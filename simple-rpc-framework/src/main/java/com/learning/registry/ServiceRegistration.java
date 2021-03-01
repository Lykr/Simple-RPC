package com.learning.registry;

import com.learning.properties.ServiceProperties;

import java.util.List;

public interface ServiceRegistration {
    /**
     * Register service
     *
     * @param serviceProperties
     * @return
     */
    boolean registerService(ServiceProperties serviceProperties);

    /**
     * Deregister service
     *
     * @param serviceProperties
     * @return
     */
    boolean deregisterService(ServiceProperties serviceProperties);

    /**
     * Get registered service names list
     *
     * @return registered service names list
     */
    List<String> getRegisteredServiceNamesList();
}
