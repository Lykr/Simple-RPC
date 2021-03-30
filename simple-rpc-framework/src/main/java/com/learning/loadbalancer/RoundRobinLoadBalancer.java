package com.learning.loadbalancer;

import com.learning.exception.NoServerException;
import com.learning.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RoundRobinLoadBalancer implements LoadBalancer {
    ConcurrentHashMap<String, Integer> serviceName2lastServerIdx = new ConcurrentHashMap<>();

    @Override
    public InetSocketAddress getSocketAddress(List<InetSocketAddress> inetSocketAddresses, RpcRequest request) throws NoServerException {
        if (inetSocketAddresses == null || inetSocketAddresses.size() == 0) {
            log.info("There is no server for this service.");
            throw new NoServerException();
        }

        String serviceName = request.getServiceName();

        int lastServerIdx = serviceName2lastServerIdx.computeIfAbsent(serviceName, k -> -1);

        lastServerIdx = (lastServerIdx + 1) % inetSocketAddresses.size();
        return inetSocketAddresses.get(lastServerIdx);
    }
}
