package com.learning.provider;

import com.learning.exception.RpcException;
import com.learning.properties.ServiceProperties;
import com.learning.registry.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service provider
 */
public interface ServiceProvider {
    /**
     * Add service to service provider
     * @param properties service properties
     * @param instance service instance
     * @throws RpcException
     */
    void addService(ServiceProperties properties, Object instance) throws RpcException;

    /**
     * Get service instance
     * @param properties service properties
     * @return service instance
     * @throws RpcException
     */
    Object getService(ServiceProperties properties) throws RpcException;
}
