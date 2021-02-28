package com.learning.registry.zookeeper;

import com.learning.properties.ServiceProperties;
import com.learning.registry.ServiceRegistry;
import com.learning.registry.zookeeper.helper.CuratorHelper;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {
    private final CuratorHelper curatorHelper;
    private final InetSocketAddress localHostSocketAddress;
    // {serviceName: serviceSocketAddresses}
    private final Map<String, List<String>> servicesSocketAddressMap;
    private final Set<String> registeredServiceNamesSet;

    public ZkServiceRegistry(InetSocketAddress inetSocketAddress) {
        localHostSocketAddress = inetSocketAddress;
        log.info("Loading ZooKeeper configuration...");
        ZooKeeperConfig zkConfig = new ZooKeeperConfig();

        log.info("Creating CuratorHelper...");
        curatorHelper = new CuratorHelper(zkConfig);

        servicesSocketAddressMap = new ConcurrentHashMap<>();
        registeredServiceNamesSet = ConcurrentHashMap.newKeySet();
    }

    @Override
    public boolean registerService(ServiceProperties serviceProperties) {
        String serviceName = serviceProperties.getRpcServiceName();
        String serverSocketAddress = localHostSocketAddress.getHostString() + ":" + localHostSocketAddress.getPort();

        try {
            if (registeredServiceNamesSet.contains(serviceName)) {
                log.info("This server already has a service named {}.", serviceName);
                return true;
            } else {
                if (curatorHelper.createNode(serviceName, serverSocketAddress)) {
                    registeredServiceNamesSet.add(serviceName);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deregisterService(ServiceProperties serviceProperties) {
        String serviceName = serviceProperties.getRpcServiceName();
        String serverSocketAddress = localHostSocketAddress.getHostString() + ":" + localHostSocketAddress.getPort();

        if (curatorHelper.deleteNode(serviceName, serverSocketAddress)) {
            registeredServiceNamesSet.remove(serviceName);
            return true;
        }

        return false;
    }

    @Override
    public List<String> getRegisteredServiceNamesList() {
        return new ArrayList<>(registeredServiceNamesSet);
    }

    @Override
    public List<InetSocketAddress> lookUpService(ServiceProperties serviceProperties) {
        String serviceName = serviceProperties.getRpcServiceName();

        log.info("Look up service named {}.", serviceName);

        List<String> childrenNodes = null;
        if (servicesSocketAddressMap.containsKey(serviceName)) {
            childrenNodes = servicesSocketAddressMap.get(serviceName);
        } else {
            childrenNodes = curatorHelper.getChildrenNodes(serviceName);
            servicesSocketAddressMap.put(serviceName, childrenNodes);
            curatorHelper.addWatcher(serviceName, servicesSocketAddressMap);
        }

        List<InetSocketAddress> res = new ArrayList<>(childrenNodes.size());
        for (String node : childrenNodes) {
            String[] arr = node.split(":");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(arr[0], Integer.parseInt(arr[1]));
            res.add(inetSocketAddress);
        }
        return res;
    }
}
