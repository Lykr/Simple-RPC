package com.learning.remoting.transport.socket;

import com.learning.remoting.handler.RpcRequestHandler;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
@Component
@Scope("prototype")
public class RpcSocketRequestHandlerRunnable implements Runnable {
    private final Socket socket;
    @Autowired
    private RpcRequestHandler rpcRequestHandler;

    public RpcSocketRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        log.info("Server handle request from client {} at thread {}.", socket.getInetAddress().getHostAddress(), Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            // 1. Get request
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 2. Call request handler get result
            Object result = rpcRequestHandler.handle(rpcRequest);
            // 3. Make result PRC response
            RpcResponse response = new RpcResponse(result);
            // 4. Send Response
            objectOutputStream.writeObject(response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
