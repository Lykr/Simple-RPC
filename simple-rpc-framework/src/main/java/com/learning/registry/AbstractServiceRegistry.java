package com.learning.registry;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractServiceRegistry implements ServiceRegistration, ServiceDiscovery {
    protected final InetSocketAddress localHostSocketAddress;
    // {serviceName: serviceSocketAddresses}
    protected final Map<String, List<String>> servicesSocketAddressMap;
    protected final Set<String> registeredServiceNamesSet;

    public AbstractServiceRegistry(InetSocketAddress localHostSocketAddress) {
        this.localHostSocketAddress = localHostSocketAddress;
        this.servicesSocketAddressMap = new ConcurrentHashMap<>();
        this.registeredServiceNamesSet = ConcurrentHashMap.newKeySet();
    }
}
