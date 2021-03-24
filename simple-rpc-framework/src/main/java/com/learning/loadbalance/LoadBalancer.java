package com.learning.loadbalance;

import com.learning.exception.NoServerException;

import java.net.InetSocketAddress;
import java.util.List;

public interface LoadBalancer {

    /**
     * Load balance (Choose one socket address from list)
     *
     * @param inetSocketAddresses socket address list
     * @return socket address
     */
    InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses, String serviceName) throws NoServerException;
}
