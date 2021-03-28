package com.learning.registry;

import java.net.InetSocketAddress;
import java.util.List;

public interface ServiceDiscovery {
    /**
     * Look up service in registry center
     *
     * @param rpcServiceName
     * @return A list of InetSocketAddresses
     */
    List<InetSocketAddress> lookUpService(String rpcServiceName);
}
