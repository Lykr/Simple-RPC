package com.learning.remoting.transport;

import com.learning.loadbalance.LoadBalance;
import com.learning.registry.ServiceDiscovery;
import com.learning.remoting.dto.RpcRequest;
import com.learning.serializer.Serializer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractClient {
    @Autowired
    protected ServiceDiscovery serviceDiscovery;
    @Autowired
    protected Serializer serializer;

    protected AbstractClient() {
    }

    /**
     * Remote procedure call
     *
     * @param request rpc request
     * @return result object
     */
    protected abstract Object remoteProcedureCall(RpcRequest request, LoadBalance loadBalance);
}
