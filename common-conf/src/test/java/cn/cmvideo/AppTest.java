package cn.cmvideo;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import cn.cmvideo.aspirin.common.conf.ConfClient;

/**
 * Unit test for simple App.
 */
@SpringBootApplication 
public class AppTest implements CommandLineRunner
{
	
	@Autowired
	ConfClient confClient;
	
    public static void main( String[] args )
    {
        System.out.println( "fuck World!" );
        ConfigurableApplicationContext ctx=SpringApplication.run(AppTest.class, args);
        ctx.close();
    }

	@Override
	public void run(String... args) throws Exception {
		
		
		
		//confClient.save("test_key", "v2222", "/ns1", "");
		//System.out.println(confClient.get("test_key", "/ns1", ""));
		
		
		String key="test_acl1.key2";
		String value="test_acl1_value1";
		
		List<ACL> acls=buildAclList();
		//confClient.zookeeperConnector.getClient().setACL().withACL(acls);
		//confClient.zookeeperConnector.getClient().create().creatingParentsIfNeeded().forPath(namespace+"/"+key, value.getBytes());
		
		System.out.println(confClient.get(key));
	}
    
	
	private List<ACL> buildAclList() throws NoSuchAlgorithmException{
		Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin123"));  
		ACL acl=new ACL(ZooDefs.Perms.ALL, id1);
		List<ACL> acls = new ArrayList<ACL>(); 
		acls.add(acl);
		return acls;
		
		
	}
    
}

