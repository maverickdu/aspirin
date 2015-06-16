package cn.cmvideo.aspirin.common.base;

import javax.annotation.PostConstruct;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
	@PropertySource("classpath:config/base/config.properties"),
	@PropertySource("classpath:config/base/zookeeper.properties")
})
public class ZookeeperConnector {
	
	@Value("${zookeeper.url}")
	private String zookeeperUrl;
	@Value("${zookeeper.baseSleepTimeMs}")
	private int baseSleepTimeMs;
	@Value("${zookeeper.maxRetries}")
	private int maxRetries;
	@Value("${zookeeper.sessionTimeoutMs}")
	private int sessionTimeoutMs;
	@Value("${zookeeper.namespace}")
	private String namespace;
	
	
	private CuratorFramework client;
	private RetryPolicy retryPolicy;
	
	@PostConstruct
	private void init(){
		
		retryPolicy=new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
		
		client=CuratorFrameworkFactory.builder()
				.connectString(zookeeperUrl)
				.sessionTimeoutMs(sessionTimeoutMs)
				.retryPolicy(retryPolicy)
				.namespace(namespace)
				//.authorization(authInfos)
				.build();
		
	}
	
	public CuratorFramework getClient(){
		if(client.getState().compareTo(CuratorFrameworkState.STARTED)!=0){
			client.start();
		}
		//client.blockUntilConnected();
		
		
		return client;
	}
	
	public void close(){
		try{
			client.close();
		}catch(Exception e){}
	}
}
