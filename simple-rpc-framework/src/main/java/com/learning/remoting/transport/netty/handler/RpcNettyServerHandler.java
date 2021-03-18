package com.learning.remoting.transport.netty.handler;

import com.learning.handler.RpcRequestHandler;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RpcNettyServerHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private RpcRequestHandler rpcRequestHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            // 1. Get request
            RpcRequest request = (RpcRequest) msg;
            // 2. Call method and get result
            Object result = rpcRequestHandler.handle(request);
            // 3. Make response
            RpcResponse rpcResponse = RpcResponse.builder().data(result).build();
            ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("An idle connection was found, it will be closed.");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
