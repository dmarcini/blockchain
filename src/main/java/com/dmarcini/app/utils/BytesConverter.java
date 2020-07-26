package com.dmarcini.app.utils;

import java.io.*;

public class BytesConverter {
    public static <T> byte[] toBytes(T object) throws IOException {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(object);

        return byteArrayOutputStream.toByteArray();
    }

    public static <T> T fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        var byteArrayInputStream = new ByteArrayInputStream(bytes);
        var objectInputStream = new ObjectInputStream(byteArrayInputStream);

        @SuppressWarnings("unchecked")
        T object = (T) objectInputStream.readObject();

        return object;
    }
}
