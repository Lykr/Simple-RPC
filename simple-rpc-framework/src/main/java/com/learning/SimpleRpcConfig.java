package com.learning;

import com.learning.config.RpcClientConfig;
import com.learning.config.RpcServerConfig;
import com.learning.loadbalance.LoadBalancer;
import com.learning.loadbalance.RandomLoadBalancer;
import com.learning.loadbalance.RoundRobinLoadBalancer;
import com.learning.registry.ServiceDiscovery;
import com.learning.remoting.transport.RpcClient;
import com.learning.remoting.transport.RpcServer;
import com.learning.remoting.transport.netty.client.RpcNettyClient;
import com.learning.remoting.transport.netty.server.RpcNettyServer;
import com.learning.remoting.transport.socket.RpcSocketClient;
import com.learning.remoting.transport.socket.RpcSocketServer;
import com.learning.serializer.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ComponentScan({"com.learning"})
public class SimpleRpcConfig {
    @Autowired
    RpcClientConfig rpcClientConfig;
    @Autowired
    RpcServerConfig rpcServerConfig;
    @Autowired
    ServiceDiscovery serviceDiscovery;
    @Autowired
    Serializer serializer;

    @Lazy
    @Bean
    public LoadBalancer loadBalancer() {
        String type = rpcClientConfig.getLoadbalancer();
        LoadBalancer loadBalancer;
        switch (type) {
            case "random":
                loadBalancer = new RandomLoadBalancer();
                break;
            case "robin":
                loadBalancer = new RoundRobinLoadBalancer();
                break;
            default:
                loadBalancer = new RandomLoadBalancer();
        }
        return loadBalancer;
    }

    @Lazy
    @Bean
    public RpcClient rpcClient() {
        String type = rpcClientConfig.getType();
        RpcClient rpcClient;
        switch (type) {
            case "netty":
                rpcClient = new RpcNettyClient();
                break;
            case "socket":
                rpcClient = new RpcSocketClient();
                break;
            default:
                rpcClient = new RpcSocketClient();
        }
        return rpcClient;
    }

    @Lazy
    @Bean
    public RpcServer rpcServer() {
        String type = rpcServerConfig.getType();
        RpcServer rpcServer;
        switch (type) {
            case "netty":
                rpcServer = new RpcNettyServer();
                break;
            case "socket":
                rpcServer = new RpcSocketServer();
                break;
            default:
                rpcServer = new RpcSocketServer();
        }
        return rpcServer;
    }
}
