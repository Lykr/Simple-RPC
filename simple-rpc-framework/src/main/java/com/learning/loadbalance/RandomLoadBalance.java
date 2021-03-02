package com.learning.loadbalance;

import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

@Component
public class RandomLoadBalance implements LoadBalance {
    @Override
    public InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses) {
        Random random = new Random();
        return inetSocketAddresses.get(random.nextInt(inetSocketAddresses.size()));
    }
}
