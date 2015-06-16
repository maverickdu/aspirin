package cn.cmvideo.aspirin.common.conf;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

import cn.cmvideo.aspirin.common.base.ZookeeperConnector;
import cn.cmvideo.aspirin.common.conf.annotation.ConfCache;


public abstract class ConfProxy {

	/**
	 * 配置用Bean中property和key的对应关系
	 * Map<filedName,conf_key>
	 */
	private static Map<Field,String> propKeyMap;
	
	private static Map<String, String> cacheMap;
	
	@Autowired
	private ConfClient confClient;
	@Autowired
	private ZookeeperConnector zooConn;
	
	private static Map<String, NodeCache> nodeCacheMap;
	
	@PostConstruct
	protected void initBean() throws Exception{
		if (nodeCacheMap==null) nodeCacheMap=new HashMap<String, NodeCache>();
		initPropKeyMap();
		initConfCacheFields();
	}
	@PreDestroy
	protected void preDestory(){
		for(NodeCache nodeCache:nodeCacheMap.values()){
			try {
				nodeCache.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean saveByKey(String key,Object value){
		boolean result=confClient.save(key, JSONObject.toJSONString(value));
		if (result){
			cacheMap.put(key, JSONObject.toJSONString(value));
		}
		
		return result;
	}
	public boolean saveByProperty(String property,Object value)
			throws NoSuchFieldError, IllegalAccessException, InvocationTargetException{
		Field targetField=null;
		for(Field field:propKeyMap.keySet()){
			if (field.getName().equals(property)){
				targetField=field;
				break;
			}
		}
		if (targetField==null){
			throw new NoSuchFieldError();
		}
		String key=propKeyMap.get(targetField);
		if (key==null){
			return false;
		}
		boolean result= saveByKey(key,value);
		if (result)
			BeanUtils.copyProperty(this, property, value);
		
		return result;
	}
	
	
	public String getFromCache(String key){
		return cacheMap.get(key);
	}
	
	/**
	 * 动态添加一个ConfCache
	 * @param key
	 * @return value value的json_string
	 * @throws Exception
	 */
	public String addConfCache(final String key) throws Exception{
		if(cacheMap==null) cacheMap=new HashMap<String,String>();
		if (cacheMap.containsKey(key)) return cacheMap.get(key);
		
		final NodeCache nodeCache=new NodeCache(zooConn.getClient(), confClient.getNodePathByKey(key));
		nodeCache.start(true);
		nodeCacheMap.put(key, nodeCache);
		
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				String newStringValue=null;
				if (nodeCache.getCurrentData()!=null){
					newStringValue=new String(nodeCache.getCurrentData().getData());
				}
				
				//String newStringValue=new String(nodeCache.getCurrentData().getData());
				cacheMap.put(key, newStringValue);
				/**
				 * 如果该key被静态注册过，则需要更新相关的property
				 */
				for(Field field:propKeyMap.keySet()){
					String theKey=propKeyMap.get(field);
					if (key.equals(theKey)){
						setConfCacheField(field, newStringValue);
					}
				}
			}
		});
		
		if (nodeCache.getCurrentData()==null) return null;
		String value=new String(nodeCache.getCurrentData().getData());
		cacheMap.put(key, value);
		
		return value;
	}
	
	
	
	private void initPropKeyMap(){
		if(propKeyMap==null) propKeyMap=new HashMap<Field,String>();
		Field[] fields=getClass().getDeclaredFields();
		for(final Field field:fields){
			if(!field.isAnnotationPresent(ConfCache.class)) continue;
			propKeyMap.put(field, field.getAnnotation(ConfCache.class).key());
		}
	}
	private void initConfCacheFields() throws Exception{
		for(Field field:propKeyMap.keySet()){
			String key=propKeyMap.get(field);
			String value=addConfCache(key);
			if (!Strings.isNullOrEmpty(value)){
				//JSONObject.parseObject(value,TestVO.class);
				BeanUtils.setProperty(this, field.getName(),JSONObject.parseObject(value,field.getType()));
			}else if(!Strings.isNullOrEmpty(field.getAnnotation(ConfCache.class).default_value())){
				BeanUtils.setProperty(this, field.getName(),field.getAnnotation(ConfCache.class).default_value());
			}
		}
	}
	private void setConfCacheField(Field field,String strValue) throws Exception{
		BeanUtils.setProperty(this, field.getName(), JSONObject.parseObject(strValue, field.getType()));
	}
	
	
}
