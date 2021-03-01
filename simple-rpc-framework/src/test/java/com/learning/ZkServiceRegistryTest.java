package com.learning;

import com.learning.properties.RpcServiceProperties;
import com.learning.registry.zookeeper.ZkServiceRegistry;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.InetSocketAddress;

public class ZkServiceRegistryTest {
    ApplicationContext context = new AnnotationConfigApplicationContext(SimpleRpcConfig.class);

    InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 1234);
    ZkServiceRegistry registry = context.getBean(ZkServiceRegistry.class);
    RpcServiceProperties rpcServiceProperties = new RpcServiceProperties("com.learning.test", "a");
    String serviceName = rpcServiceProperties.getRpcServiceName();

    @Test
    public void registryTest() throws InterruptedException {
        registry.registerService(serviceName);
        Thread.sleep(1000);
        registry.registerService(serviceName);
        Thread.sleep(1000);
    }

    @Test
    public void deleteTest() throws InterruptedException {
        registry.registerService(serviceName);
        Thread.sleep(1000);
        registry.deregisterService(serviceName);
    }
}
