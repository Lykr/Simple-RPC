package com.learning.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public final class ThreadPoolFactory {
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    private ThreadPoolFactory() {

    }

    public static ExecutorService createThreadPoolIfAbsent(String threadNamePrefix) {
        ThreadPoolConfig config = new ThreadPoolConfig();
        return createThreadPool(config, threadNamePrefix, false);
    }

    public static ExecutorService createThreadPoolIfAbsent(ThreadPoolConfig customThreadPoolConfig, String threadNamePrefix) {
        return createThreadPool(customThreadPoolConfig, threadNamePrefix, false);
    }

    /**
     * Create thread pool by threadNamePrefix
     * If there is not thread pool named threadNamePrefix in THREAD_POOLS, create one.
     * If the thread pool is terminated or shutdown, create one.
     * Otherwise, return the one in THREAD_POOLS already.
     * @param threadPoolConfig
     * @param threadNamePrefix
     * @param daemon
     * @return
     */
    public static ExecutorService createThreadPoolIfAbsent(ThreadPoolConfig threadPoolConfig, String threadNamePrefix, Boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadPoolConfig, threadNamePrefix, daemon));

        if (threadPool.isTerminated() || threadPool.isShutdown()) {
            threadPool = createThreadPool(threadPoolConfig, threadNamePrefix, daemon);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }

        return threadPool;
    }

    /**
     * Create Thread Pool
     * @param customThreadPoolConfig
     * @param threadNamePrefix
     * @param daemon
     * @return
     */
    private static ExecutorService createThreadPool(ThreadPoolConfig customThreadPoolConfig, String threadNamePrefix, Boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(customThreadPoolConfig.getCorePoolSize(), customThreadPoolConfig.getMaximumPoolSize(),
                customThreadPoolConfig.getKeepAliveTime(), customThreadPoolConfig.getUnit(), customThreadPoolConfig.getWorkQueue(),
                threadFactory);
    }

    /**
     * Create thread factory
     * Create a customized factory if threadNamePrefix is not null.
     * Otherwise, create a default factory.
     * @param threadNamePrefix
     * @param daemon
     * @return
     */
    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(daemon).build();
            }
        }
        return Executors.defaultThreadFactory();
    }
}
