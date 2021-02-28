package com.learning.provider;

import com.learning.properties.ServiceProperties;

import java.util.List;

/**
 * Service provider
 */
public interface ServiceProvider {
    /**
     * Add service to service provider
     *
     * @param serviceProperties service properties
     * @param instance          service instance
     * @return Is the service added successful?
     */
    boolean addService(ServiceProperties serviceProperties, Object instance);

    /**
     * Get service instance
     *
     * @param serviceProperties service properties
     * @return service instance
     */
    Object getService(ServiceProperties serviceProperties);

    /**
     * Get provided service names list
     *
     * @return provided service names list
     */
    List<String> getProvidedServiceNamesList();
}
