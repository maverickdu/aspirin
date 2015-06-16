package cn.cmvideo.aspirin.common.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cmvideo.aspirin.common.base.ZookeeperConnector;

@Component
public class ConfClient {
	
	@Autowired
	private ZookeeperConnector zookeeperConnector;
	
	/**
	 * key的形式为xxx.yyy.zzz.key,映射到zookeeper中的目录为/namespace/xxx/yyy/zzz/key,
	 * 其中namesapce为zookeeper_client指定的namespace
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean save(String key,String value){
		try {
			//EnsurePath ensurePath=new EnsurePath(getNodePathByKey(key));
			//ensurePath.ensure(zookeeperConnector.getClient().getZookeeperClient());
			
			zookeeperConnector.getClient().newNamespaceAwareEnsurePath(getNodePathByKey(key))
				.ensure(zookeeperConnector.getClient().getZookeeperClient());
			
			
			zookeeperConnector.getClient().setData().forPath(getNodePathByKey(key), value.getBytes());
			//zookeeperConnector.getClient().create().creatingParentsIfNeeded().forPath(getNodePathByKey(key), value.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean remove(String key){
		
		return true;
	}
	public String get(String key){
		
		try {
			byte[] data=zookeeperConnector.getClient().getData().forPath(getNodePathByKey(key));
			return new String(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public String getNodePathByKey(String key){
		return "/"+key.replaceAll("\\.", "/");
	}
	
	
}
