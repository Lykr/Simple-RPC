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

    /**
     * Get thread pool by thread name prefix
     * Create new one if absent.
     * @param threadNamePrefix
     * @return
     */
    public static ExecutorService getThreadPool(String threadNamePrefix) {
        if (!THREAD_POOLS.containsKey(threadNamePrefix)) {
            log.info("There is not {} thread pool in THREAD_POOLS, create new one.", threadNamePrefix);
            createThreadPoolIfAbsent(threadNamePrefix);
        }
        return THREAD_POOLS.get(threadNamePrefix);
    }

    public static void createThreadPoolIfAbsent(String threadNamePrefix) {
        ThreadPoolConfig config = new ThreadPoolConfig();
        createThreadPool(config, threadNamePrefix, false);
    }

    public static void createThreadPoolIfAbsent(ThreadPoolConfig customThreadPoolConfig, String threadNamePrefix) {
        createThreadPool(customThreadPoolConfig, threadNamePrefix, false);
    }

    /**
     * Create thread pool by threadNamePrefix
     * If there is not thread pool named threadNamePrefix in THREAD_POOLS, create one.
     * If the thread pool is terminated or shutdown, create one.
     * Otherwise, return the one in THREAD_POOLS already.
     * @param threadPoolConfig
     * @param threadNamePrefix
     * @param daemon
     */
    public static void createThreadPoolIfAbsent(ThreadPoolConfig threadPoolConfig, String threadNamePrefix, Boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadPoolConfig, threadNamePrefix, daemon));

        if (threadPool.isTerminated() || threadPool.isShutdown()) {
            log.info("{} thread pool is shutdown or terminated, create new one.", threadNamePrefix);
            threadPool = createThreadPool(threadPoolConfig, threadNamePrefix, daemon);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }
    }

    /**
     * Create Thread Pool
     * @param customThreadPoolConfig
     * @param threadNamePrefix
     * @param daemon
     * @return Thread pool
     */
    private static ExecutorService createThreadPool(ThreadPoolConfig customThreadPoolConfig, String threadNamePrefix, Boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        log.info("Create {} thread pool.", threadNamePrefix);
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
     * @return Thread factory
     */
    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
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
