package com.learning.provider;

import java.util.List;

/**
 * Service provider
 */
public interface ServiceProvider {
    /**
     * Add service to service provider
     *
     * @param serviceName service name
     * @param obj         service object
     */
    void addService(String serviceName, Object obj);

    /**
     * Get service instance
     *
     * @param serviceName service name
     * @return service instance
     */
    Object getService(String serviceName);

    /**
     * Remove service
     *
     * @param serviceName
     */
    void removeService(String serviceName);

    /**
     * Get provided service names list
     *
     * @return provided service names list
     */
    List<String> getProvidedServiceNamesList();
}
