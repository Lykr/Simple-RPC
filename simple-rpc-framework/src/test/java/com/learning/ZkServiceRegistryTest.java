package com.learning;

import com.learning.properties.ServiceProperties;
import com.learning.registry.zookeeper.ZkServiceRegistry;
import org.junit.Test;

import java.net.InetSocketAddress;

public class ZkServiceRegistryTest {
    InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 1234);
    ZkServiceRegistry registry = new ZkServiceRegistry(socketAddress);
    ServiceProperties serviceProperties = new ServiceProperties("com.learning.test", "a");

    @Test
    public void registryTest() throws InterruptedException {
        registry.registerService(serviceProperties);
        Thread.sleep(1000);
        registry.registerService(serviceProperties);
        Thread.sleep(1000);
    }

    @Test
    public void deleteTest() throws InterruptedException {
        registry.registerService(serviceProperties);
        Thread.sleep(1000);
        registry.deregisterService(serviceProperties);
    }
}
