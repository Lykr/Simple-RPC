package com.learning.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class RpcServerConfig {
    private static final int DEFAULT_PORT = 6161;
    private static final String DEFAULT_THREAD_NAME_PREFIX = "socket-rpc-server";

    private int port = DEFAULT_PORT;
    private String threadNamePrefix = DEFAULT_THREAD_NAME_PREFIX;
}
