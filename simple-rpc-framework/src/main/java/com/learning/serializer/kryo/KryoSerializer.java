package com.learning.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.learning.serializer.Serializer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class KryoSerializer implements Serializer {
    // Kryo is not thread safe, in a multithreaded environment ThreadLocal might be considered.
    // An other way is pooling the kryo.
    private static final ThreadLocal<Kryo> kryos = ThreadLocal.withInitial(Kryo::new);

    @Override
    public byte[] serialize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryos.get();
            kryo.writeObject(output, object);
            kryos.remove();
            return output.toBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryos.get();
            Object object = kryo.readObject(input, clazz);
            kryos.remove();
            return clazz.cast(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
