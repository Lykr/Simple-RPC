package com.learning.loadbalancer;

import com.learning.exception.NoServerException;
import com.learning.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;
import java.util.List;

public interface LoadBalancer {

    /**
     * Load balance (Choose one socket address from list)
     *
     * @param inetSocketAddresses socket address list
     * @return socket address
     */
    InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses, RpcRequest request) throws NoServerException;
}
