package com.learning;

import com.learning.properties.RpcServiceProperties;
import com.learning.remoting.transport.RpcServer;
import com.learning.remoting.transport.netty.server.RpcNettyServer;
import com.learning.service.impl.EchoServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RpcServerApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SimpleRpcConfig.class);
        RpcServer server = context.getBean(RpcNettyServer.class);

        server.addService(new RpcServiceProperties("com.learning.service.EchoService"), new EchoServiceImpl());

        server.start();
    }
}
