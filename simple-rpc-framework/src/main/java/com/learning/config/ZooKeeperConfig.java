package com.learning.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@PropertySource(value = {"classpath:rpc.properties"}, encoding = "UTF-8")
public class ZooKeeperConfig {
    private static final String DEFAULT_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_PORT = 2181;
    private static final String DEFAULT_ROOT_PATH = "/simple-rpc";
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final int DEFAULT_BASE_SLEEP_TIME_MS = 1000;
    private static final int DEFAULT_SESSION_TIMEOUT_MS = 5000;
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 5000;

    @Value("${zookeeper.address}")
    private String address = DEFAULT_ADDRESS;
    @Value("${zookeeper.port}")
    private int port = DEFAULT_PORT;
    @Value("${zookeeper.rootPath}")
    private String rootPath = DEFAULT_ROOT_PATH;
    @Value("${zookeeper.maxRetries}")
    private int maxRetries = DEFAULT_MAX_RETRIES;
    @Value("${zookeeper.baseSleepTimeMs}")
    private int baseSleepTimeMs = DEFAULT_BASE_SLEEP_TIME_MS;
    @Value("${zookeeper.sessionTimeoutMs}")
    private int sessionTimeoutMs = DEFAULT_SESSION_TIMEOUT_MS;
    @Value("${zookeeper.connectionTimeoutMs}")
    private int connectionTimeoutMs = DEFAULT_CONNECTION_TIMEOUT_MS;

}
