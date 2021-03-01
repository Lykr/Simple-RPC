package com.learning.registry.zookeeper.helper;

import com.learning.config.ZooKeeperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CuratorHelper {
    private final ZooKeeperConfig zkConfig;
    private final CuratorFramework zkClient;

    public CuratorHelper(ZooKeeperConfig zooKeeperConfig) {
        zkConfig = zooKeeperConfig;

        log.info("Trying to connect ZooKeeper server...");
        zkClient = createZooKeeperClient(zooKeeperConfig);
        zkClient.start();
    }

    public boolean createNode(String serviceName, String serviceSocketAddress) {
        String serviceSocketAddressPath = zkConfig.getRootPath() + "/" + serviceName + "/" + serviceSocketAddress;

        try {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(serviceSocketAddressPath);
            log.info("Create node {} successfully.", serviceSocketAddressPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Fail to Create node {}.", serviceSocketAddressPath);
            return false;
        }

        return true;
    }

    public List<String> getChildrenNodes(String serviceName) {

        List<String> list = null;
        String servicePath = zkConfig.getRootPath() + "/" + serviceName;
        try {
            list = zkClient.getChildren().forPath(servicePath);
            log.info("Get children nodes list {} for path {}.", list, servicePath);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Fail to get children nodes for path {}.", servicePath);
        }
        return list;
    }

    public boolean deleteNode(String serviceName, String serviceSocketAddress) {
        String serviceSocketAddressPath = zkConfig.getRootPath() + "/" + serviceName + "/" + serviceSocketAddress;
        try {
            zkClient.delete().forPath(serviceSocketAddressPath);
            log.info("Delete node for path {}.", serviceSocketAddressPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Fail to delete node for path {}.", serviceSocketAddressPath);
            return false;
        }
        return true;
    }

    private CuratorFramework createZooKeeperClient(ZooKeeperConfig zooKeeperConfig) {
        int baseSleepTimeMs = zooKeeperConfig.getBaseSleepTimeMs();
        int maxRetries = zooKeeperConfig.getMaxRetries();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);

        String connectString = zooKeeperConfig.getAddress() + ":" + zooKeeperConfig.getPort();
        int sessionTimeoutMs = zooKeeperConfig.getSessionTimeoutMS();
        int connectionTimeoutMs = zooKeeperConfig.getConnectionTimeoutMs();

        return CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
    }

    public void addWatcher(String serviceName, Map<String, List<String>> servicesSocketAddressMap) {
        String servicePath = zkConfig.getRootPath() + "/" + serviceName;
        PathChildrenCache cache = new PathChildrenCache(zkClient, servicePath, true);
        cache.getListenable().addListener((client, event) -> {
            log.info("Path {} changed.", servicePath);
            List<String> list = client.getChildren().forPath(servicePath);
            servicesSocketAddressMap.put(serviceName, list);
        });
    }

    //private void startClient() {
    //    if (zkClient.getState() == CuratorFrameworkState.STARTED) {
    //        return;
    //    }
    //    try {
    //        int retries = zkConfig.getMaxRetries();
    //        int maxWaitTime = zkConfig.getConnectionTimeoutMs();
    //
    //        for (int i = 0; i <= retries; i++) {
    //            zkClient.start();
    //            if (i > 0) log.info("Retry to connect ZooKeeper server, round {}.", i);
    //            if (!zkClient.blockUntilConnected(maxWaitTime, TimeUnit.MILLISECONDS)) {
    //                log.info("ZooKeeper Client cannot connect to Zookeeper server.");
    //            } else {
    //                break;
    //            }
    //            if (i == retries) throw new RuntimeException("Time out to connect ZooKeeper server.");
    //        }
    //    } catch (InterruptedException e) {
    //        e.printStackTrace();
    //    }
    //}


}
