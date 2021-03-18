package com.learning.remoting.transport.netty.client;

import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import com.learning.remoting.transport.AbstractClient;
import com.learning.remoting.transport.netty.coder.RpcNettyDecoder;
import com.learning.remoting.transport.netty.coder.RpcNettyEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RpcNettyClient extends AbstractClient {
    private final ChannelProvider channelProvider;
    private final RequestChecker requestChecker;

    public RpcNettyClient() {
        this.requestChecker = new RequestChecker();
        // Set up Netty client
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        this.channelProvider = new ChannelProvider(bootstrap);
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new IdleStateHandler(0, 10, 0, TimeUnit.SECONDS))
                                .addLast(new RpcNettyEncoder(serializer, RpcRequest.class))
                                .addLast(new RpcNettyDecoder(serializer, RpcResponse.class))
                                .addLast(new RpcNettyClientHandler(requestChecker, channelProvider));
                    }
                });
    }

    @Override
    public Object call(RpcRequest request) {
        // Async return result
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        // Service discovery
        String serviceName = request.getServiceName();
        List<InetSocketAddress> addresses = serviceDiscovery(serviceName);
        // Load Balance
        InetSocketAddress address = loadBalance.getSocketAddress(addresses, serviceName);
        // Get channel of address
        Channel channel = channelProvider.getChannel(address);
        if (channel.isActive()) {
            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        requestChecker.put(request.getRequestId(), resultFuture);
                        log.info("Send request: {}", request.toString());
                    } else {
                        future.channel().close();
                        resultFuture.completeExceptionally(future.cause());
                        log.info("Fail to send request {}: {}", request.toString(), future.cause().toString());
                    }
                }
            });
        }
        Object res = null;
        try {
            res = resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }
}
