package com.learning.provider.impl;

import com.learning.exception.RpcException;
import com.learning.properties.ServiceProperties;
import com.learning.provider.ServiceProvider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProviderImpl implements ServiceProvider {
    protected Map<String, Object> serviceMap;

    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean addService(ServiceProperties properties, Object instance) throws RpcException {
        return false;
    }

    @Override
    public Object getService(ServiceProperties properties) throws RpcException {
        return null;
    }

    @Override
    public List<String> getProvidedServiceNamesList() {
        return null;
    }
}
