package com.learning;

import com.learning.properties.RpcServiceProperties;
import com.learning.remoting.dto.RpcRequest;
import com.learning.serializer.kryo.KryoSerializer;
import org.junit.Test;

import java.util.Arrays;

public class KryoTest {

    @Test
    public void test() {
        KryoSerializer kryoSerializer = new KryoSerializer();
        RpcRequest request = new RpcRequest();
        request.setRpcServiceProperties(new RpcServiceProperties("com.learning.a", null));
        byte[] bytes = kryoSerializer.serialize(request);
        System.out.println(Arrays.toString(bytes));
        RpcRequest res = kryoSerializer.deserialize(bytes, RpcRequest.class);
        System.out.println(res.getRpcServiceProperties().getRpcServiceName());
    }
}

class Hello {
    private String hello = "hello";
    private String name = "no one";

    public String sayHello() {
        return hello + ", " + name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
