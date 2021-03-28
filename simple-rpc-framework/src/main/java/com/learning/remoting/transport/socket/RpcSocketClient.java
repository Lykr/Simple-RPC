package com.learning.remoting.transport.socket;

import com.learning.config.RpcClientConfig;
import com.learning.loadbalance.LoadBalancer;
import com.learning.registry.ServiceDiscovery;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import com.learning.remoting.transport.AbstractClient;
import com.learning.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Slf4j
//@Conditional({SocketClientCondition.class})
//@Component
public class RpcSocketClient extends AbstractClient {

    @Override
    public Object call(RpcRequest request) {

        InetSocketAddress socketAddress = getServiceAddress(request);

        String serverAddress = socketAddress.getHostString() + ":" + socketAddress.getPort();
        Object res = null;
        try (Socket socket = new Socket()) {
            // 3. Connect server
            socket.connect(socketAddress, 5000);
            log.info("Connect to server {}.", serverAddress);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // 4. Send request
            log.info("Try to send request.");
            objectOutputStream.writeObject(request);
            // 5. Get response
            RpcResponse response = (RpcResponse) objectInputStream.readObject();
            // 6. Parse response
            res = response.getData();
            log.info("Get response from server {} for service {}.", serverAddress, request.getServiceName());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            log.info("Fail to connect server {}.", serverAddress);
        }
        // 7. Return result
        return res;
    }
}
