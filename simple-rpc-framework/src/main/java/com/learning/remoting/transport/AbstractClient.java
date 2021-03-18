package com.learning.remoting.transport;

import com.learning.config.RpcClientConfig;
import com.learning.loadbalance.LoadBalance;
import com.learning.loadbalance.RandomLoadBalance;
import com.learning.loadbalance.RoundRobinLoadBalance;
import com.learning.registry.ServiceDiscovery;
import com.learning.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
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

    protected LoadBalance loadBalance;
    protected ApplicationContext context;

    protected AbstractClient() {
    }

    @PostConstruct
    public void setLoadBalancer() {
        String loadBalance = rpcClientConfig.getLoadBalance();
        if ("robin".equals(loadBalance)) {
            this.loadBalance = context.getBean(RoundRobinLoadBalance.class);
        } else {
            this.loadBalance = context.getBean(RandomLoadBalance.class);
        }
        log.info("Use {} load balance.", loadBalance);
    }

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
