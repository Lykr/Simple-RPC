package com.learning.remoting.transport;

import com.learning.registry.ServiceDiscovery;
import com.learning.serializer.Serializer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractClient implements RpcClient {
    @Autowired
    protected ServiceDiscovery serviceDiscovery;
    @Autowired
    protected Serializer serializer;

    protected AbstractClient() {
    }
}
