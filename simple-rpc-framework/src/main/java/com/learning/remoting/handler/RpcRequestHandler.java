package com.learning.remoting.handler;

import com.learning.provider.ServiceProvider;
import com.learning.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Slf4j
public class RpcRequestHandler {
    @Autowired
    private ServiceProvider serviceProvider;

    public Object handle(RpcRequest request) {
        // 1. Get service name
        String serviceName = request.getInterfaceName() + ":" + request.getVersion();
        // 2. Get service object
        Object serviceObject = serviceProvider.getService(serviceName);
        // 3. Get method
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParamTypes();
        // 4. Invoke method and get result
        Object res = null;
        Object[] parameters = request.getParameters();
        try {
            Method method = serviceObject.getClass().getMethod(methodName, parameterTypes);
            res = method.invoke(serviceObject, parameters);
            log.info("Invoked {}.{}({}) successfully.", serviceName, methodName, parameters);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            log.info("Fail to invoked {}.{}({}).", serviceName, methodName, parameters);
        }
        return res;
    }

}
