package com.learning.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@PropertySource(value = {"classpath:rpc.properties"}, encoding = "UTF-8")
public class ThreadPoolConfig {
    /**
     * Default parameters
     */
    private static final int DEFAULT_CORE_POOL_SIZE = 10;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE_SIZE = 100;
    private static final int DEFAULT_KEEP_ALIVE_TIME = 1;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
    private static final int DEFAULT_BLOCKING_QUEUE_CAPACITY = 100;

    /**
     * Configurable parameters
     */
    @Value("${server.threadPool.corePoolSize}")
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    @Value("${server.threadPool.maximumPoolSize}")
    private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE_SIZE;
    @Value("${server.threadPool.keepAliveTime}")
    private long keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
    @Value("${server.threadPool.timeUnit}")
    private String timeUnitString = null;
    private TimeUnit unit = DEFAULT_TIME_UNIT;

    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(DEFAULT_BLOCKING_QUEUE_CAPACITY);

    @PostConstruct
    private void setTimeUnitByString() {
        if (timeUnitString != null) {
            switch (timeUnitString) {
                case "MINUTES":
                    unit = TimeUnit.MINUTES;
                    break;
                case "SECONDS":
                    unit = TimeUnit.SECONDS;
                    break;
                // ... more cases ..
                default:
                    throw new RuntimeException("No this time unit: " + timeUnitString + "!");
            }
        }
    }
}
