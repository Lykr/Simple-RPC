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
public class RpcServerConfig {
    private static final int DEFAULT_PORT = 6161;
    private static final String DEFAULT_THREAD_NAME_PREFIX = "socket-rpc-server";
    private static final String DEFAULT_TYPE = "socket";

    @Value("${server.port}")
    private int port = DEFAULT_PORT;

    @Value("${server.threadNamePrefix}")
    private String threadNamePrefix = DEFAULT_THREAD_NAME_PREFIX;

    @Value("${server.type}")
    private String type = DEFAULT_TYPE;
}
