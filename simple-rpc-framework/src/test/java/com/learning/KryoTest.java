package com.learning;

import com.learning.serializer.kryo.KryoSerializer;
import org.junit.Test;

import java.util.Arrays;

public class KryoTest {

    @Test
    public void test() {
        KryoSerializer kryoSerializer = new KryoSerializer();
        Hello hello = new Hello();
        hello.setName("Chen");
        byte[] bytes = kryoSerializer.serialize(hello);
        System.out.println(Arrays.toString(bytes));
        Hello res = kryoSerializer.deserialize(bytes, Hello.class);
        System.out.println(res.sayHello());
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
