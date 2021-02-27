package com.learning.provider.impl;

import com.learning.exception.RpcException;
import com.learning.properties.ServiceProperties;
import com.learning.provider.ServiceProvider;
import com.learning.registry.ServiceRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProviderImpl implements ServiceProvider {
    protected Map<String, Object> serviceMap;
    protected ServiceRegistry serviceRegistry;

    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addService(ServiceProperties properties, Object instance) throws RpcException {

    }

    @Override
    public Object getService(ServiceProperties properties) throws RpcException {
        return null;
    }

    private void registerService(ServiceProperties properties, Object instance) throws RpcException {

    }
}
