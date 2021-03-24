package com.learning.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Lazy
@Component
@PropertySource(value = {"classpath:rpc.properties"}, encoding = "UTF-8")
public class RpcClientConfig {
    private static final String DEFAULT_LOADBALANCER = "random";
    private static final String DEFAULT_TYPE = "socket";

    @Value("${client.loadBalancer}")
    private String loadbalancer = DEFAULT_LOADBALANCER;

    @Value("${client.type}")
    private String type = DEFAULT_TYPE;
}
