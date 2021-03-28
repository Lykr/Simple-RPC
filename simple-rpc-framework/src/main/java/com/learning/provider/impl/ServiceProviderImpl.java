package com.learning.provider.impl;

import com.learning.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ServiceProviderImpl implements ServiceProvider {
    protected final Map<String, Object> serviceMap;

    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addService(String serviceName, Object obj) {
        if (serviceMap.containsKey(serviceName)) {
            log.info("{} service already exists, no need to add.", serviceName);
        } else {
            serviceMap.put(serviceName, obj);
            log.info("Added {} service.", serviceName);
        }
    }

    @Override
    public Object getService(String serviceName) {
        if (!serviceMap.containsKey(serviceName)) {
            log.info("There is no service named {}, get fail.", serviceName);
            return null;
        }
        return serviceMap.get(serviceName);
    }

    @Override
    public void removeService(String serviceName) {
        if (!serviceMap.containsKey(serviceName)) {
            log.info("There is no service named {}, remove fail.", serviceName);
        } else {
            serviceMap.remove(serviceName);
        }
    }

    @Override
    public List<String> getProvidedServiceNamesList() {
        return new ArrayList<String>(serviceMap.keySet());
    }
}
