package com.learning.remoting.transport;

import com.learning.properties.RpcServiceProperties;

public interface RpcServer {
    /**
     * Add new service
     *
     * @param properties
     * @param obj
     * @return Is the service added successfully?
     */
    boolean addService(RpcServiceProperties properties, Object obj);

    /**
     * Remove service
     *
     * @param properties
     * @return Is the service removed successfully?
     */
    boolean removeService(RpcServiceProperties properties);

    /**
     * Start server, handle rpc request
     */
    void start();
}
