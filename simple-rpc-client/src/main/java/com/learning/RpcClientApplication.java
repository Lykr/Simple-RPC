package com.learning;

import com.learning.proxy.ServiceProxyFactory;
import com.learning.service.EchoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RpcClientApplication {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(SimpleRpcConfig.class);
        EchoService echo = context.getBean(ServiceProxyFactory.class).getProxy(EchoService.class);
        System.out.println(echo.echo("Hello~"));
        System.out.println(echo.echo("Hello~"));
        System.out.println(echo.echo("Hello~"));
    }
}
