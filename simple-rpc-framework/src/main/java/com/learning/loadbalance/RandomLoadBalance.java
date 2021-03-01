package com.learning.loadbalance;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {
    @Override
    public InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses) {
        Random random = new Random();
        return inetSocketAddresses.get(random.nextInt(inetSocketAddresses.size()));
    }
}
