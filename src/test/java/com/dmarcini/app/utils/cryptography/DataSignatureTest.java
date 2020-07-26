package com.dmarcini.app.utils.cryptography;

import com.dmarcini.app.utils.BytesConverter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Comparator;

class DataSignatureTest {
    private static final String KEYS_DIR = "keys/";
    private static final String PUBLIC_KEY_PAIR_PATH = KEYS_DIR + "public-key-from-pair";
    private static final String PRIVATE_KEY_PAIR_PATH = KEYS_DIR + "private-key-from-pair";
    private static final String PRIVATE_KEY_NOT_PAIR_PATH = KEYS_DIR + "private-key-not-from-pair";

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException, IOException {
        KeysGenerator keysGenerator = new KeysGenerator(1024);

        keysGenerator.generateKeys();

        if (!new File(KEYS_DIR).mkdir()) {
            throw new IOException("Cannot create dir!");
        }

        keysGenerator.saveToFile(PUBLIC_KEY_PAIR_PATH, keysGenerator.getPublicKey().getEncoded());
        keysGenerator.saveToFile(PRIVATE_KEY_PAIR_PATH, keysGenerator.getPrivateKey().getEncoded());

        keysGenerator.generateKeys();

        keysGenerator.saveToFile(PRIVATE_KEY_NOT_PAIR_PATH, keysGenerator.getPrivateKey().getEncoded());
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.walk(Paths.get(KEYS_DIR))
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
    }

    @Test
    void signVerify_signAndVerifyCustomObject_succeed() throws InvalidKeySpecException, SignatureException,
                                                               NoSuchAlgorithmException, InvalidKeyException,
                                                               IOException {
        var testObject = new TestClass("Lorem ipsum dolor sit", 2020);

        var signature = DataSignature.sign(BytesConverter.toBytes(testObject), PRIVATE_KEY_PAIR_PATH);

        Assertions.assertTrue(DataSignature.verify(BytesConverter.toBytes(testObject), signature,
                                                   PUBLIC_KEY_PAIR_PATH));
    }

    @Test
    void signVerify_signAndVerifyCustomObject_Failed() throws InvalidKeySpecException, SignatureException,
                                                              NoSuchAlgorithmException, InvalidKeyException,
                                                              IOException {
        var testObject = new TestClass("Lorem ipsum dolor sit", 2020);

        var signature = DataSignature.sign(BytesConverter.toBytes(testObject), PRIVATE_KEY_NOT_PAIR_PATH);

        Assertions.assertFalse(DataSignature.verify(BytesConverter.toBytes(testObject), signature,
                                                    PUBLIC_KEY_PAIR_PATH));
    }

    private static class TestClass implements Serializable {
        String text;
        int number;

        TestClass(String text, int number) {
            this.text = text;
            this.number = number;
        }
    }

}