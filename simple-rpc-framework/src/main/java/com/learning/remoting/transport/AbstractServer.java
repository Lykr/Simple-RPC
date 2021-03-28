package com.learning.remoting.transport;


import com.learning.config.RpcServerConfig;
import com.learning.factory.ThreadPoolFactory;
import com.learning.properties.RpcServiceProperties;
import com.learning.provider.ServiceProvider;
import com.learning.registry.ServiceRegistration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

public abstract class AbstractServer implements RpcServer {
    protected String LOCAL_HOST_ADDRESS; // Local host address
    protected int PORT; // Server port
    @Autowired
    protected ServiceProvider serviceProvider; // Service provider for adding and getting service
    @Autowired
    protected ServiceRegistration serviceRegistration; // Service registry for registering service
    @Autowired
    protected RpcServerConfig rpcServerConfig; // Service registry for registering service
    @Autowired
    protected ThreadPoolFactory threadPoolFactory;

    @PostConstruct
    public void setProperties() {
        try {
            this.LOCAL_HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.PORT = rpcServerConfig.getPort();
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
