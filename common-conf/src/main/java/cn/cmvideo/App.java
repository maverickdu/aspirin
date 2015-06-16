package cn.cmvideo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;




import com.alibaba.fastjson.JSONObject;

import cn.cmvideo.aspirin.common.conf.ConfClient;
import cn.cmvideo.test.ConfBeanTest;


/**
 * Hello world!
 *
 */
@SpringBootApplication 
@Configuration
public class App implements CommandLineRunner
{
	
	@Autowired
	ConfClient confClient;
	
	@Value("${common.conf.packages}")
	private String commonConfScanPkgs;
	@Autowired
	ConfBeanTest ttt;
	
	
	static Logger logger=LogManager.getLogger(App.class.getName());
	
	
    public static void main( String[] args ) throws IOException
    {
        System.out.println( "fuck World!" );
        ConfigurableApplicationContext ctx=SpringApplication.run(App.class, args);
        
        
        //logger.entry();
        logger.debug("------Hello, World!122");
        logger.error("Hello, World!2++++");
        //logger.exit();
        
        //System.in.read();
        ctx.close();
        
        
        
    }

	@Override
	public void run(String... args) throws Exception {
		
		
		 
		//confClient.save("test_key", "v2222", "/ns1", "");
		//System.out.println(confClient.get("test_key", "/ns1", ""));
		
		
		String key="test_acl1.key2";
		String value="test_acl1_value1";
		
		//List<ACL> acls=buildAclList();
		//confClient.zookeeperConnector.getClient().setACL().withACL(acls);
		//confClient.zookeeperConnector.getClient().create().creatingParentsIfNeeded().forPath(namespace+"/"+key, value.getBytes());
		
		//System.out.println(confClient.get(key));
		
		ttt.saveByProperty("url", "www.google.com.hk");
		while(true){
			System.out.println(System.currentTimeMillis()+"--------"+ttt.getUrl()+"\t\t"+ttt.getAbc()+"\t\t"+ttt.getEee()+"\t\t"+JSONObject.toJSONString(ttt.getTestVO()));
			Thread.sleep(3000);
		}
		
		//ttt.saveByKey("test_acl1.eee", 123);
		//TestVO test=new TestVO();
		//ttt.saveByKey("test_acl1.testVO", test);
		
		//System.out.println("--------"+ttt.getUrl());
	}
    
	
	private List<ACL> buildAclList() throws NoSuchAlgorithmException{
		Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin123"));  
		ACL acl=new ACL(ZooDefs.Perms.ALL, id1);
		List<ACL> acls = new ArrayList<ACL>(); 
		acls.add(acl);
		return acls;
		
		
	}
    
}
