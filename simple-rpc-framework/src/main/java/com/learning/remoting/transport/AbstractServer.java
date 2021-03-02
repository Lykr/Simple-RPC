package com.learning.remoting.transport;


import com.learning.config.RpcServerConfig;
import com.learning.factory.ThreadPoolFactory;
import com.learning.properties.RpcServiceProperties;
import com.learning.provider.ServiceProvider;
import com.learning.registry.ServiceRegistration;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

public abstract class AbstractServer implements RpcServer {
    protected final String LOCAL_HOST_ADDRESS; // Local host address
    protected final int PORT; // Server port
    protected ExecutorService threadPool; // Thread pool for running service
    @Autowired
    protected ServiceProvider serviceProvider; // Service provider for adding and getting service
    @Autowired
    protected ServiceRegistration serviceRegistration; // Service registry for registering service
    protected RpcServerConfig rpcServerConfig; // Service registry for registering service

    protected AbstractServer(RpcServerConfig rpcServerConfig, ThreadPoolFactory threadPoolFactory) throws UnknownHostException {
        this.rpcServerConfig = rpcServerConfig;
        this.LOCAL_HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        this.PORT = rpcServerConfig.getPort();
        this.threadPool = threadPoolFactory.getThreadPool(rpcServerConfig.getThreadNamePrefix());
    }
}
