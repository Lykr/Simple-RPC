package com.learning.proxy.handler;

import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.transport.RpcClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
@Component
public class RpcInvocationHandler implements InvocationHandler {
    @Autowired
    RpcClient rpcClient;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Invoke method {}.", method.getName());
        // 1. Build RPC request
        RpcRequest request = RpcRequest.builder().
                interfaceName(method.getDeclaringClass().getName()).
                methodName(method.getName()).
                paramTypes(method.getParameterTypes()).
                parameters(args).
                build();
        // 2. RPC
        Object obj = rpcClient.call(request);
        if (obj == null) {
            log.info("Fail to invoke method.");
        }
        return obj;
    }
}
