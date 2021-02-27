package com.learning.remoting.transport;


import com.learning.exception.RpcException;
import com.learning.properties.ServiceProperties;
import com.learning.provider.ServiceProvider;

import java.util.concurrent.ExecutorService;

public abstract class AbstractServer {
    protected final int PORT; // Server port
    protected ExecutorService threadpool; // Threadpool for running service
    protected ServiceProvider serviceProvider; // Service provider for adding and getting service

    protected AbstractServer(int port) {
        this.PORT = port;
    }

    public abstract void registerService(ServiceProperties properties, Object serviceInstance) throws RpcException;

    public abstract void start();

}
