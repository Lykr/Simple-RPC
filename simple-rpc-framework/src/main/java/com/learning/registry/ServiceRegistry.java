package com.learning.registry;

import com.learning.properties.ServiceProperties;

import java.net.InetSocketAddress;
import java.util.List;

public interface ServiceRegistry {
    /**
     * Register service
     * @param serviceProperties
     * @return
     */
    boolean registerService(ServiceProperties serviceProperties);

    /**
     * Deregister service
     * @param serviceProperties
     * @return
     */
    boolean deregisterService(ServiceProperties serviceProperties);

    /**
     * Look up service in registry center
     *
     * @param serviceProperties
     * @return A list of InetSocketAddresses
     */
    List<InetSocketAddress> lookUpService(ServiceProperties serviceProperties);

    /**
     * Get registered service names list
     *
     * @return registered service names list
     */
    List<String> getRegisteredServiceNamesList();
}
