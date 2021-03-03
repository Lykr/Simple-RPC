package com.learning.loadbalance;

import java.net.InetSocketAddress;
import java.util.List;

public interface LoadBalance {

    /**
     * Load balance (Choose one socket address from list)
     *
     * @param inetSocketAddresses socket address list
     * @return socket address
     */
    InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses, String serviceName);
}
