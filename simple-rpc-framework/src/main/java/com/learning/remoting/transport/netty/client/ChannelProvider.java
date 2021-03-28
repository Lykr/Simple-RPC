package com.learning.remoting.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ChannelProvider {
    private final Map<InetSocketAddress, Channel> map;
    private final Bootstrap bootstrap;

    public ChannelProvider(Bootstrap bootstrap) {
        this.map = new ConcurrentHashMap<>();
        this.bootstrap = bootstrap;
    }

    public Channel getChannel(InetSocketAddress address) {
        Channel channel = map.get(address);
        if (channel == null) {
            channel = createChannel(address);
        } else if (!channel.isActive()) {
            channel = createChannel(address);
            map.put(address, channel);
        }
        return channel;
    }

    private Channel createChannel(InetSocketAddress address) {
        Channel channel = null;
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(address).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected {} successfully", address.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        try {
            channel = completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
