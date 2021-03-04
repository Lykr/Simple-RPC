package com.learning.remoting.transport.netty.coder;

import com.learning.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcNettyDecoder extends ByteToMessageDecoder {
    private final Serializer kryo;
    private final Class<?> messageClass;

    public RpcNettyDecoder(Serializer kryo, Class<?> messageClass) {
        this.kryo = kryo;
        this.messageClass = messageClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) return;
        in.markReaderIndex();
        int len = in.readInt();
        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[len];
        in.readBytes(data);

        Object obj = kryo.deserialize(data, messageClass);
        out.add(obj);
    }
}
