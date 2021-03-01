package com.learning.remoting.transport.socket;

import com.learning.loadbalance.LoadBalance;
import com.learning.properties.RpcServiceProperties;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import com.learning.remoting.transport.AbstractClient;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Component
public class RpcSocketClient extends AbstractClient {
    @Override
    protected Object remoteProcedureCall(RpcRequest request, LoadBalance loadBalance) {
        // 1. Service discovery
        RpcServiceProperties serviceProperties = request.getRpcServiceProperties();
        String serviceName = serviceProperties.getRpcServiceName();
        List<InetSocketAddress> serviceSocketAddresses = serviceDiscovery.lookUpService(serviceName);

        // 2. Load balance
        InetSocketAddress socketAddress = loadBalance.getSocketAddress(serviceSocketAddresses);

        Object res = null;
        try (Socket socket = new Socket();
             OutputStream outputStream = socket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             InputStream inputStream = socket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            // 3. Connect server
            socket.connect(socketAddress);
            // 4. Send request
            objectOutputStream.writeObject(request);
            // 5. Get response
            RpcResponse response = (RpcResponse) objectInputStream.readObject();
            // 6. Parse response
            res = response.getData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 7. Return result
        return res;
    }
}
