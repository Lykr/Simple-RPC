package com.learning.remoting.transport.netty.server;

import com.learning.config.RpcServerConfig;
import com.learning.factory.ThreadPoolFactory;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import com.learning.remoting.transport.AbstractServer;
import com.learning.remoting.transport.netty.coder.RpcNettyDecoder;
import com.learning.remoting.transport.netty.coder.RpcNettyEncoder;
import com.learning.serializer.kryo.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class RpcNettyServer extends AbstractServer {
    @Autowired
    KryoSerializer kryoSerializer;
    @Autowired
    RpcNettyServerHandler rpcNettyServerHandler;

    protected RpcNettyServer(RpcServerConfig rpcServerConfig, ThreadPoolFactory threadPoolFactory) throws UnknownHostException {
        super(rpcServerConfig, threadPoolFactory);
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors() * 2,
                threadPoolFactory.createThreadFactory("server-handler-group", false));
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // Set TCP_NODELAY to true to close Nagle algorithm (Wait to send a bigger packet)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // HeartBeat
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // Store the tcp connection which completed shake hand
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // Log handler, running after bootstrap init
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // Channel handler, running after channel set up (client connect)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new RpcNettyEncoder(kryoSerializer, RpcResponse.class))
                                    .addLast(new RpcNettyDecoder(kryoSerializer, RpcRequest.class))
                                    .addLast(serviceHandlerGroup, rpcNettyServerHandler);
                        }
                    });
            ChannelFuture f = b.bind(LOCAL_HOST_ADDRESS, PORT).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }

    }
}
