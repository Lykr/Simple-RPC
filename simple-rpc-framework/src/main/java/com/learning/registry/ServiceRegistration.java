package com.learning.registry;

import java.util.List;

public interface ServiceRegistration {
    /**
     * Register service
     *
     * @param rpcServiceName
     * @return
     */
    boolean registerService(String rpcServiceName);

    /**
     * Deregister service
     *
     * @param rpcServiceName
     * @return
     */
    boolean deregisterService(String rpcServiceName);

    /**
     * Get registered service names list
     *
     * @return registered service names list
     */
    List<String> getRegisteredServiceNamesList();
}
