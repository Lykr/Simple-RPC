package com.learning;

import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class OtherTest {

    @Test
    public void printLocalInetAddress() {
        try {
            System.out.println(new InetSocketAddress("127.0.0.1", 1234).getAddress());
            System.out.println(new InetSocketAddress("127.0.0.1", 1234).getHostString());
            System.out.println(new InetSocketAddress("127.0.0.1", 1234).toString());
            System.out.println(InetAddress.getLocalHost().toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
