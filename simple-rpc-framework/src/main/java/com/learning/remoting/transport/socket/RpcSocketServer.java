package com.learning.remoting.transport.socket;

import com.learning.config.RpcServerConfig;
import com.learning.factory.ThreadPoolFactory;
import com.learning.properties.RpcServiceProperties;
import com.learning.remoting.transport.AbstractServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

@Slf4j
@Component
public class RpcSocketServer extends AbstractServer implements ApplicationContextAware {
    private ApplicationContext context;

    @Autowired
    protected RpcSocketServer(RpcServerConfig rpcServerConfig, ThreadPoolFactory threadPoolFactory) throws UnknownHostException {
        super(rpcServerConfig, threadPoolFactory);
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

    @Override
    public void start() {
        log.info("Server start.");
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(LOCAL_HOST_ADDRESS, PORT));
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("Client {} connected.", socket.getInetAddress());
                threadPool.execute(context.getBean(RpcSocketRequestHandlerRunnable.class, socket));
            }
            threadPool.shutdown();
            log.info("Server {} shutdown.", LOCAL_HOST_ADDRESS + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
