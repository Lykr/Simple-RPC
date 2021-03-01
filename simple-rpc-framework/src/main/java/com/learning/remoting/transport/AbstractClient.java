package com.learning.remoting.transport;

import com.learning.registry.ServiceDiscovery;
import com.learning.registry.zookeeper.ZkServiceRegistry;
import com.learning.remoting.RpcConfig;

public abstract class AbstractClient {
    protected final ServiceDiscovery serviceDiscovery;

    protected AbstractClient(RpcConfig config) {
        this.serviceDiscovery = new ZkServiceRegistry(null);
    }
}
