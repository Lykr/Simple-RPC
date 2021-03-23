package com.learning.remoting.transport.socket;

import com.learning.config.RpcServerConfig;
import com.learning.factory.ThreadPoolFactory;
import com.learning.remoting.transport.AbstractServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

@Slf4j
//@Component
public class RpcSocketServer extends AbstractServer implements ApplicationContextAware {
    private ApplicationContext context;
    private ExecutorService threadPool; // Thread pool for running service

    @PostConstruct
    public void setThreadPool() {
        this.threadPool = threadPoolFactory.getThreadPool(rpcServerConfig.getThreadNamePrefix());
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
