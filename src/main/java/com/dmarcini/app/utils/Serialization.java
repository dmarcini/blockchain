package com.dmarcini.app.utils;

import java.io.*;
import java.util.Optional;

public class Serialization {
    private static final String DEFAULT_SERIALIZED_OBJECT_NAME = "object.bin";

    public static <T> void serialize(String path, T object) throws IOException {
        String filePath = (path == null || path.isEmpty()) ? DEFAULT_SERIALIZED_OBJECT_NAME : path;

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePath))) {
            output.writeObject(object);
        }
    }

    public static <T> Optional<T> deserialize(String path) throws ClassNotFoundException {
        String filePath = (path == null || path.isEmpty()) ? DEFAULT_SERIALIZED_OBJECT_NAME : path;

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath))) {
            @SuppressWarnings("unchecked")
            var object = Optional.of((T) input.readObject());

            return object;
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
