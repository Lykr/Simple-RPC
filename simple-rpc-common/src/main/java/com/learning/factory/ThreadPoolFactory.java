package com.learning.factory;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public final class ThreadPoolFactory {
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    private ThreadPoolFactory() {

    }

    public static ExecutorService createThreadPool(String threadPoolName) {
        return null;
    }
}
