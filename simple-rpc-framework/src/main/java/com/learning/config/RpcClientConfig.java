package com.learning.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource(value = {"classpath:rpc.properties"}, encoding = "UTF-8")
public class RpcClientConfig {
    private static final String DEFAULT_LOADBALANCE = "random";

    @Value("${client.loadBalance}")
    private String loadBalance = DEFAULT_LOADBALANCE;
}
