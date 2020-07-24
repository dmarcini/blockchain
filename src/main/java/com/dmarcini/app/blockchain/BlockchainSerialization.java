package com.dmarcini.app.blockchain;

import java.io.*;
import java.util.Optional;

public class BlockchainSerialization {
    final static private String FILENAME = "blockchain.bin";

    public static void serialize(String path, Blockchain blockchain) throws IOException {
        String filePath;

        if (path == null || path.isEmpty()) {
            filePath = FILENAME;
        } else {
            filePath = path + "\\" + FILENAME;
        }

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePath))) {
            output.writeObject(blockchain);
        }
    }

    public static Optional<Blockchain> deserialize(String path) throws ClassNotFoundException {
        String filePath;

        if (path == null || path.isEmpty()) {
            filePath = FILENAME;
        } else {
            filePath = path + "\\" + FILENAME;
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath))) {
            return Optional.of((Blockchain) input.readObject());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
