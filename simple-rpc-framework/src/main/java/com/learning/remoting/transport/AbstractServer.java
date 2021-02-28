package com.learning.remoting.transport;


import com.learning.factory.ThreadPoolFactory;
import com.learning.properties.ServiceProperties;
import com.learning.provider.ServiceProvider;
import com.learning.provider.impl.ServiceProviderImpl;
import com.learning.registry.ServiceRegistry;
import com.learning.registry.zookeeper.ZkServiceRegistry;
import com.learning.remoting.RpcConfig;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

public abstract class AbstractServer {
    protected final String LOCAL_HOST_ADDRESS; // Local host address
    protected final int PORT; // Server port
    protected ExecutorService threadPool; // Thread pool for running service
    protected ServiceProvider serviceProvider; // Service provider for adding and getting service
    protected ServiceRegistry serviceRegistry; // Service registry for registering service

    protected AbstractServer(RpcConfig rpcConfig) throws UnknownHostException {
        this.LOCAL_HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        this.PORT = rpcConfig.getPort();
        this.threadPool = ThreadPoolFactory.getThreadPool(rpcConfig.getThreadNamePrefix());
        this.serviceProvider = new ServiceProviderImpl();
        this.serviceRegistry = new ZkServiceRegistry(new InetSocketAddress(LOCAL_HOST_ADDRESS, PORT));
    }

    /**
     * Add new service
     *
     * @param properties
     * @param serviceClass
     * @return Is the service added successfully?
     */
    public abstract boolean addService(ServiceProperties properties, Class<?> serviceClass);

    /**
     * Remove service
     *
     * @param properties
     * @return Is the service removed successfully?
     */
    public abstract boolean removeService(ServiceProperties properties);

    /**
     * Start server
     */
    public abstract void start();

}
