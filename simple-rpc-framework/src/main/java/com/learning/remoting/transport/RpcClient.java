package com.learning.remoting.transport;

import com.learning.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;
import java.util.List;

public interface RpcClient {
    /**
     * Remote procedure call
     *
     * @param request rpc request
     * @return result object
     */
    Object call(RpcRequest request);

    /**
     * Discover service
     * @param serviceName
     * @return InetSocketAddresses of the service
     */
    List<InetSocketAddress> serviceDiscovery(String serviceName);
}
