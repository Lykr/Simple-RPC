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
    @Autowired
    protected ServiceProvider serviceProvider; // Service provider for adding and getting service
    @Autowired
    protected ServiceRegistration serviceRegistration; // Service registry for registering service
    protected RpcServerConfig rpcServerConfig; // Service registry for registering service
    protected ThreadPoolFactory threadPoolFactory;

    protected AbstractServer(RpcServerConfig rpcServerConfig, ThreadPoolFactory threadPoolFactory) throws UnknownHostException {
        this.rpcServerConfig = rpcServerConfig;
        this.LOCAL_HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        this.PORT = rpcServerConfig.getPort();
        this.threadPoolFactory = threadPoolFactory;
    }

    @Override
    public boolean addService(RpcServiceProperties properties, Object obj) {
        // 1. Get service name
        String serviceName = properties.getRpcServiceName();
        // 2. Add service to service provider
        serviceProvider.addService(serviceName, obj);
        // 3. Registry service
        boolean success = serviceRegistration.registerService(serviceName);
        // If registry service fail, rollback serviceProvider
        if (!success) {
            serviceProvider.removeService(serviceName);
            return false;
        }

        return true;
    }

    @Override
    public boolean removeService(RpcServiceProperties properties) {
        // 1. Get service name
        String serviceName = properties.getRpcServiceName();
        // 2. Deregister service
        boolean success = serviceRegistration.deregisterService(serviceName);
        if (!success) return false;
        // 3. Remove service from service provider
        serviceProvider.removeService(serviceName);
        return true;
    }
}
