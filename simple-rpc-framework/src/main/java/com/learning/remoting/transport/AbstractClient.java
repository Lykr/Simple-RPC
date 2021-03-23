package com.learning.remoting.transport;

import com.learning.config.RpcClientConfig;
import com.learning.loadbalance.LoadBalancer;
import com.learning.registry.ServiceDiscovery;
import com.learning.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public abstract class AbstractClient implements RpcClient, ApplicationContextAware {
    @Autowired
    protected RpcClientConfig rpcClientConfig;
    @Autowired
    protected ServiceDiscovery serviceDiscovery;
    @Autowired
    protected Serializer serializer;
    @Autowired
    protected LoadBalancer loadBalancer;

    protected ApplicationContext context;

    // @PostConstruct
    // public void setLoadBalancer() {
    //     String loadBalancer = rpcClientConfig.getLoadBalance();
    //     if ("robin".equals(loadBalancer)) {
    //         this.loadBalancer = context.getBean(RoundRobinLoadBalancer.class);
    //     } else {
    //         this.loadBalancer = context.getBean(RandomLoadBalancer.class);
    //     }
    //     log.info("Use {} load balance.", loadBalancer);
    // }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public List<InetSocketAddress> serviceDiscovery(String serviceName) {
        List<InetSocketAddress> serviceSocketAddresses = serviceDiscovery.lookUpService(serviceName);
        if (serviceSocketAddresses == null) {
            log.info("No found server for {}.", serviceName);
        }
        return serviceSocketAddresses;
    }
}
