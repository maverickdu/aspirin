package cn.cmvideo.test;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.cmvideo.aspirin.common.conf.ConfProxy;
import cn.cmvideo.aspirin.common.conf.annotation.ConfCache;

@Component
@Scope("singleton")
public class ConfBeanTest extends ConfProxy{
	
	
	
	@ConfCache(key="test_acl1.key2")
	private String url;

	
	@ConfCache(key="test_acl1.abc",default_value="abc_value")
	private String abc;
	
	@ConfCache(key="test_acl1.eee")
	private int eee;
	
	
	@ConfCache(key="test_acl1.testVO")
	private TestVO testVO;
	
	
	public void setUrl(String url){
		this.url=url;
	}
	public String getUrl(){
		return url;
	}
	public String getAbc() {
		return abc;
	}
	public void setAbc(String abc) {
		this.abc = abc;
	}
	public int getEee() {
		return eee;
	}
	public void setEee(int eee) {
		this.eee = eee;
	}
	
	public TestVO getTestVO() {
		return testVO;
	}
	public void setTestVO(TestVO testVO) {
		this.testVO = testVO;
	}
	
	
	
	public static void main(String[] args)throws Exception{
		System.out.println(JSONObject.toJSONString("dfd"));
		
		System.out.println(JSONObject.parseObject(JSONObject.toJSONString("dfd"), String.class));
		
	}
	
}
