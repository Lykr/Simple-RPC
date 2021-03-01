package com.learning.registry;

import com.learning.config.ZooKeeperConfig;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractServiceRegistry implements ServiceRegistration, ServiceDiscovery {
    protected final InetSocketAddress localHostSocketAddress;
    // {serviceName: serviceSocketAddresses}
    protected final Map<String, List<String>> servicesSocketAddressMap;
    protected final Set<String> registeredServiceNamesSet;

    public AbstractServiceRegistry(ZooKeeperConfig zooKeeperConfig) throws UnknownHostException {
        InetSocketAddress localHostSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), zooKeeperConfig.getPort());
        this.localHostSocketAddress = localHostSocketAddress;
        this.servicesSocketAddressMap = new ConcurrentHashMap<>();
        this.registeredServiceNamesSet = ConcurrentHashMap.newKeySet();
    }
}
