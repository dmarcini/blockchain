package com.dmarcini.app.utils.cryptography;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

public class RSAKeysGenerator {
    private final KeyPairGenerator keyPairGenerator;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAKeysGenerator(int keyLength) throws NoSuchAlgorithmException {
        this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        this.keyPairGenerator.initialize(keyLength);
    }

    public void generateKeys() {
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void saveToFile(String path, byte[] key) {
        try (FileOutputStream output = new FileOutputStream(new File(path))) {
            output.write(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
