package com.learning.loadbalance;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

@Slf4j
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses, String serviceName) {
        if (inetSocketAddresses == null || inetSocketAddresses.size() == 0) {
            log.info("There is no server fr this service.");
            return null;
        }
        Random random = new Random();
        return inetSocketAddresses.get(random.nextInt(inetSocketAddresses.size()));
    }
}
