package com.learning.registry;

import com.learning.properties.ServiceProperties;

import java.net.InetSocketAddress;
import java.util.List;

public interface ServiceDiscovery {
    /**
     * Look up service in registry center
     *
     * @param serviceProperties
     * @return A list of InetSocketAddresses
     */
    List<InetSocketAddress> lookUpService(ServiceProperties serviceProperties);
}
