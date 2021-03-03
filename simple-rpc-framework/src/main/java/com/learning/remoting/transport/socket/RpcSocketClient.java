package com.learning.remoting.transport.socket;

import com.learning.config.RpcClientConfig;
import com.learning.loadbalance.LoadBalance;
import com.learning.loadbalance.RandomLoadBalance;
import com.learning.loadbalance.RoundRobinLoadBalance;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import com.learning.remoting.transport.AbstractClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Slf4j
@Component
public class RpcSocketClient extends AbstractClient implements ApplicationContextAware {
    @Autowired
    RpcClientConfig rpcClientConfig;
    LoadBalance loadBalance;
    ApplicationContext context;

    @PostConstruct
    public void setLoadBalance() {
        String loadBalance = rpcClientConfig.getLoadBalance();
        if ("robin".equals(loadBalance)) {
            this.loadBalance = context.getBean(RoundRobinLoadBalance.class);
        } else {
            this.loadBalance = context.getBean(RandomLoadBalance.class);
        }
        log.info("Use {} load balance.", loadBalance);
    }

    @Override
    public Object call(RpcRequest request) {
        // 1. Service discovery
        String serviceName = request.getServiceName();
        List<InetSocketAddress> serviceSocketAddresses = serviceDiscovery.lookUpService(serviceName);
        if (serviceSocketAddresses == null) {
            log.info("No found server for {}.", serviceName);
            return null;
        }

        // 2. Load balance
        InetSocketAddress socketAddress = loadBalance.getSocketAddress(serviceSocketAddresses, serviceName);

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
            log.info("Get response from server {} for service {}.", serverAddress, serviceName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            log.info("Fail to connect server {}.", serverAddress);
        }
        // 7. Return result
        return res;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
