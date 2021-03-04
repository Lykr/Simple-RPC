package com.learning.remoting.transport.netty.coder;

import com.learning.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcNettyEncoder extends MessageToByteEncoder {
    private final Serializer kryo;
    private final Class<?> messageClass;

    public RpcNettyEncoder(Serializer kryo, Class<?> messageClass) {
        this.kryo = kryo;
        this.messageClass = messageClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (messageClass.isInstance(msg)) {
            byte[] data = kryo.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
