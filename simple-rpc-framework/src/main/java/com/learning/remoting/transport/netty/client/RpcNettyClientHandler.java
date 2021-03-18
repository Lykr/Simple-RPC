package com.learning.remoting.transport.netty.client;

import com.learning.remoting.dto.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class RpcNettyClientHandler extends ChannelInboundHandlerAdapter {
    private final RequestChecker requestChecker;
    private final ChannelProvider channelProvider;

    public RpcNettyClientHandler(RequestChecker requestChecker, ChannelProvider channelProvider) {
        this.requestChecker = requestChecker;
        this.channelProvider = channelProvider;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Client receive msg: {}", msg);
        if (msg instanceof RpcResponse) {
            RpcResponse response = (RpcResponse) msg;
            requestChecker.complete(response);
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("write idle happen {}", ctx.channel().remoteAddress());
                // Send heart beat packet
                // WAIT TO BE DONE
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
