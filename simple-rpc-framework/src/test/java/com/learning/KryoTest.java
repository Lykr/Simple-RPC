package com.learning;

import com.learning.remoting.dto.RpcRequest;
import com.learning.serializer.kryo.KryoSerializer;
import org.junit.Test;

import java.util.Arrays;

public class KryoTest {

    @Test
    public void test() {
        KryoSerializer kryoSerializer = new KryoSerializer();
        RpcRequest request = RpcRequest.builder().interfaceName("com.learning.a").version(null).build();
        byte[] bytes = kryoSerializer.serialize(request);
        System.out.println(Arrays.toString(bytes));
        RpcRequest res = kryoSerializer.deserialize(bytes, RpcRequest.class);
        System.out.println(res.getInterfaceName() + ":" + res.getVersion());
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
