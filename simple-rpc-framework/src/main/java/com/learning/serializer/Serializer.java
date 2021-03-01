package com.learning.serializer;

public interface Serializer {
    /**
     * Serialize
     *
     * @param object
     * @return bytes
     */
    byte[] serialize(Object object);

    /**
     * Deserialize
     *
     * @param bytes
     * @param clazz Object class
     * @param <T>   Object class type
     * @return Object
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
