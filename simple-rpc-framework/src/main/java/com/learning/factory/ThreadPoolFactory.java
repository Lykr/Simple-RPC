package com.learning.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.learning.config.ThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
public class ThreadPoolFactory {
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();
    private final ThreadPoolConfig threadPoolConfig;

    @Autowired
    public ThreadPoolFactory(ThreadPoolConfig threadPoolConfig) {
        this.threadPoolConfig = threadPoolConfig;
    }

    /**
     * Get thread pool by thread name prefix
     * Create new one if absent.
     *
     * @param threadNamePrefix
     * @return
     */
    public ExecutorService getThreadPool(String threadNamePrefix) {
        if (!THREAD_POOLS.containsKey(threadNamePrefix)) {
            log.info("There is no {} thread pool in THREAD_POOLS, create new one.", threadNamePrefix);
            createThreadPoolIfAbsent(threadNamePrefix);
        }
        return THREAD_POOLS.get(threadNamePrefix);
    }

    public void createThreadPoolIfAbsent(String threadNamePrefix) {
        createThreadPoolIfAbsent(threadNamePrefix, false);
    }

    /**
     * Create thread pool by threadNamePrefix
     * If there is not thread pool named threadNamePrefix in THREAD_POOLS, create one.
     * If the thread pool is terminated or shutdown, create one.
     * Otherwise, return the one in THREAD_POOLS already.
     *
     * @param threadNamePrefix
     * @param daemon
     */
    public void createThreadPoolIfAbsent(String threadNamePrefix, Boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadNamePrefix, daemon));

        if (threadPool.isTerminated() || threadPool.isShutdown()) {
            log.info("{} thread pool is shutdown or terminated, create new one.", threadNamePrefix);
            threadPool = createThreadPool(threadNamePrefix, daemon);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }
    }

    /**
     * Create Thread Pool
     *
     * @param threadNamePrefix
     * @param daemon
     * @return Thread pool
     */
    private ExecutorService createThreadPool(String threadNamePrefix, Boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        log.info("Create {} thread pool.", threadNamePrefix);
        return new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(), threadPoolConfig.getMaximumPoolSize(),
                threadPoolConfig.getKeepAliveTime(), threadPoolConfig.getUnit(), threadPoolConfig.getWorkQueue(),
                threadFactory);
    }

    /**
     * Create thread factory
     * Create a customized factory if threadNamePrefix is not null.
     * Otherwise, create a default factory.
     *
     * @param threadNamePrefix
     * @param daemon
     * @return Thread factory
     */
    private ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                log.info("Create thread factory for {} thread pool.", threadNamePrefix);
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(daemon).build();
            }
        }
        log.info("Create a default thread factory.");
        return Executors.defaultThreadFactory();
    }
}
