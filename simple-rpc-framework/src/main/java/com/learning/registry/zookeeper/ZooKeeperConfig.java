package com.learning.registry.zookeeper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZooKeeperConfig {
    private static final String DEFAULT_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_PORT = 2181;
    private static final String DEFAULT_ROOT_PATH = "/simple-rpc";
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final int DEFAULT_BASE_SLEEP_TIME_MS = 1000;
    private static final int DEFAULT_SESSION_TIMEOUT_MS = 5000;
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 5000;

    private String address = DEFAULT_ADDRESS;
    private int port = DEFAULT_PORT;
    private String rootPath = DEFAULT_ROOT_PATH;
    private int maxRetries = DEFAULT_MAX_RETRIES;
    private int baseSleepTimeMs = DEFAULT_BASE_SLEEP_TIME_MS;
    private int sessionTimeoutMS = DEFAULT_SESSION_TIMEOUT_MS;
    private int connectionTimeoutMs = DEFAULT_CONNECTION_TIMEOUT_MS;

}
