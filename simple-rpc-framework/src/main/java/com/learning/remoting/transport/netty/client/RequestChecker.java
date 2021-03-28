package com.learning.remoting.transport.netty.client;

import com.learning.remoting.dto.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RequestChecker {
    private final Map<String, CompletableFuture<RpcResponse>> map;

    public RequestChecker() {
        this.map = new ConcurrentHashMap<>();
    }

    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        map.put(requestId, future);
    }

    public void complete(RpcResponse response) {
        String requestId = response.getRequestId();
        CompletableFuture<RpcResponse> future = map.get(requestId);
        if (future != null) {
            future.complete(response);
        } else {
            log.info("There is no {} request for response.", response.getRequestId());
            throw new IllegalStateException(); // Uncheck exception, no need to try-catch or throws
        }
    }
}
