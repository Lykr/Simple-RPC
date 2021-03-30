package com.learning.loadbalancer;

import com.learning.exception.NoServerException;
import com.learning.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Referring to Dubbo: https://github.com/apache/dubbo/blob/master/dubbo-cluster/src/main/java/org/apache/dubbo/rpc/cluster/loadbalance/ConsistentHashLoadBalance.java
 */
@Slf4j
public class ConsistentHashLoadBalancer implements LoadBalancer {
    private final ConcurrentHashMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    @Override
    public InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses, RpcRequest request) throws NoServerException {
        if (inetSocketAddresses == null || inetSocketAddresses.size() == 0) {
            log.info("There is no server for this service!");
            throw new NoServerException();
        }

        String serviceName = request.getServiceName();

        // Get hash of current inetSocketAddresses
        int hashOfAddresses = System.identityHashCode(inetSocketAddresses);

        // Using service's name as key for getting elements from selectors
        ConsistentHashSelector selector = selectors.get(serviceName);

        // If there is not selector for the service or the addresses list is changed, create a new selector.
        if (selector == null || selector.identityHashCode != hashOfAddresses) {
            selectors.put(serviceName, new ConsistentHashSelector(hashOfAddresses, inetSocketAddresses, 160));
            selector = selectors.get(serviceName);
        }

        return selector.select(request.getParameters());
    }

    /**
     * Inner class for ConsistentHashLoadBalancer.
     * Using as selector for specific service.
     */
    static class ConsistentHashSelector {
        private final int identityHashCode;
        private final TreeMap<Long, InetSocketAddress> virtualNodes;

        public ConsistentHashSelector(int identityHashCode, List<InetSocketAddress> inetSocketAddresses, int replicaNumber) {
            this.identityHashCode = identityHashCode;
            virtualNodes = new TreeMap<>();

            for (InetSocketAddress address : inetSocketAddresses) {
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(address.toString() + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualNodes.put(m, address);
                    }
                }
            }
        }

        private static byte[] md5(String key) {
            MessageDigest md = null;
            
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            
            return md.digest();
        }

        private static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 0xFF) << 24
                    | (long) (digest[2 + idx * 4] & 0xFF) << 16
                    | (long) (digest[1 + idx * 4] & 0xFF) << 8
                    | (long) (digest[idx * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }

        private InetSocketAddress select(Object[] parameters) {
            String key = toKey(parameters);
            byte[] digest = md5(key);
            return selectForKey(hash(digest, 0));
        }

        private String toKey(Object[] parameters) {
            StringBuilder sb = new StringBuilder();

            for (Object parameter : parameters) {
                sb.append(parameter);
            }

            return sb.toString();
        }

        private InetSocketAddress selectForKey(long hashCode) {
            Map.Entry<Long, InetSocketAddress> entry = virtualNodes.ceilingEntry(hashCode);

            if (entry == null) {
                entry = virtualNodes.firstEntry();
            }

            return entry.getValue();
        }
    }
}
