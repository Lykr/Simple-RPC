package com.learning.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@Slf4j
@Component
public class ServiceProxyFactory {
    @Autowired
    private InvocationHandler invocationHandler;

    public <T> T getProxy(Class<T> clazz) {
        log.info("Generate proxy for class {}.", clazz.getName());
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
    }

}
