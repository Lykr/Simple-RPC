package com.learning;

import com.learning.config.RpcClientConfig;
import com.learning.config.RpcServerConfig;
import com.learning.loadbalance.LoadBalancer;
import com.learning.loadbalance.RandomLoadBalancer;
import com.learning.loadbalance.RoundRobinLoadBalancer;
import com.learning.registry.ServiceDiscovery;
import com.learning.registry.zookeeper.ZkServiceRegistry;
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
        if (type.equals("random")) {
            return new RandomLoadBalancer();
        } else if (type.equals("robin")) {
            return new RoundRobinLoadBalancer();
        }
        return null;
    }

    @Lazy
    @Bean
    public RpcClient rpcClient() {
        String type = rpcClientConfig.getType();
        if (type.equals("netty")) {
            return new RpcNettyClient();
        } else if (type.equals("socket")) {
            return new RpcSocketClient();
        }
        return null;
    }

    @Lazy
    @Bean
    public RpcServer rpcServer() {
        String type = rpcServerConfig.getType();
        if (type.equals("netty")) {
            return new RpcNettyServer();
        } else if (type.equals("socket")) {
            return new RpcSocketServer();
        }
        return null;
    }
}
