package com.learning.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RoundRobinLoadBalancer implements LoadBalancer {
    ConcurrentHashMap<String, Integer> serviceName2lastServerIdx = new ConcurrentHashMap<>();

    @Override
    public InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses, String serviceName) {
        if (inetSocketAddresses == null || inetSocketAddresses.size() == 0) {
            log.info("There is no server for this service.");
            return null;
        }

        int lastServerIdx = serviceName2lastServerIdx.computeIfAbsent(serviceName, k -> -1);

        lastServerIdx = (lastServerIdx + 1) % inetSocketAddresses.size();
        return inetSocketAddresses.get(lastServerIdx);
    }
}
