package com.learning.registry.zookeeper;

import com.learning.config.RpcServerConfig;
import com.learning.config.ZooKeeperConfig;
import com.learning.registry.AbstractServiceRegistry;
import com.learning.registry.zookeeper.helper.CuratorHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ZkServiceRegistry extends AbstractServiceRegistry {
    @Autowired
    private CuratorHelper curatorHelper;

    @Autowired
    public ZkServiceRegistry(ZooKeeperConfig zooKeeperConfig, RpcServerConfig rpcServerConfig) throws UnknownHostException {
        super(zooKeeperConfig, rpcServerConfig);
    }

    @Override
    public boolean registerService(String rpcServiceName) {
        String serverSocketAddress = localHostSocketAddress.getHostString() + ":" + localHostSocketAddress.getPort();

        try {
            if (registeredServiceNamesSet.contains(rpcServiceName)) {
                log.info("This server already has a service named {}.", rpcServiceName);
                return true;
            } else {
                if (curatorHelper.createNode(rpcServiceName, serverSocketAddress)) {
                    log.info("Register service {} successfully.", rpcServiceName);
                    registeredServiceNamesSet.add(rpcServiceName);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deregisterService(String rpcServiceName) {
        String serverSocketAddress = localHostSocketAddress.getHostString() + ":" + localHostSocketAddress.getPort();

        if (curatorHelper.deleteNode(rpcServiceName, serverSocketAddress)) {
            registeredServiceNamesSet.remove(rpcServiceName);
            log.info("Deregister service {} successfully.", rpcServiceName);
            return true;
        }

        log.info("Fail to deregister service {}.", rpcServiceName);
        return false;
    }

    @Override
    public List<String> getRegisteredServiceNamesList() {
        return new ArrayList<>(registeredServiceNamesSet);
    }

    @Override
    public List<InetSocketAddress> lookUpService(String rpcServiceName) {
        log.info("Look up service named {}.", rpcServiceName);

        List<String> childrenNodes = null;
        if (servicesSocketAddressMap.containsKey(rpcServiceName)) {
            childrenNodes = servicesSocketAddressMap.get(rpcServiceName);
        } else {
            childrenNodes = curatorHelper.getChildrenNodes(rpcServiceName);
            if (childrenNodes == null) {
                log.info("There is not service named {} in registry center.", rpcServiceName);
                return null;
            }
            servicesSocketAddressMap.put(rpcServiceName, childrenNodes);
            curatorHelper.addWatcher(rpcServiceName, servicesSocketAddressMap);
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
