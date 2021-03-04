package com.learning.remoting.transport.netty.handler;

import com.learning.handler.RpcRequestHandler;
import com.learning.remoting.dto.RpcRequest;
import com.learning.remoting.dto.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
