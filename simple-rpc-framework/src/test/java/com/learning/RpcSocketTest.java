package com.learning;

import com.learning.properties.RpcServiceProperties;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.transport.socket.RpcSocketClient;
import com.learning.remoting.transport.socket.RpcSocketServer;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RpcSocketTest {
    ApplicationContext context = new AnnotationConfigApplicationContext(SimpleRpcConfig.class);

    @Test
    public void simpleTest() {
        RpcSocketServer server = context.getBean(RpcSocketServer.class);
        RpcSocketClient client = context.getBean(RpcSocketClient.class);

        server.addService(new RpcServiceProperties("com.learning.Hello", null), new Hello());
        Thread thread = new Thread(server::start);
        thread.start();

        RpcRequest rpcRequest1 = RpcRequest.builder().
                interfaceName("com.learning.A").
                version(null).
                methodName("fun").
                parameters(null).
                paramTypes(null).
                build();
        Object res1 = client.call(rpcRequest1);
        System.out.println(res1);

        RpcRequest rpcRequest2 = RpcRequest.builder().
                interfaceName("com.learning.Echo").
                version(null).
                methodName("echo").
                parameters(new Object[]{"Hello?"}).
                paramTypes(new Class[]{String.class}).
                build();
        Object res2 = client.call(rpcRequest2);
        System.out.println(res2);
        rpcRequest2.setParameters(new Object[]{"Hi!"});
        Object res3 = client.call(rpcRequest2);
        System.out.println(res3);
    }

}
