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

class ObjectSignatureTest {
    private static final String KEYS_DIR = "keys/";
    private static final String PUBLIC_KEY_FROM_PAIR_PATH = KEYS_DIR + "public-key-from-pair";
    private static final String PRIVATE_KEY_FROM_PAIR_PATH = KEYS_DIR + "private-key-from-pair";
    private static final String PRIVATE_KEY_NOT_FROM_PAIR_PATH = KEYS_DIR + "private-key-not-from-pair";

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException, IOException {
        RSAKeysGenerator RSAKeysGenerator = new RSAKeysGenerator(1024);

        RSAKeysGenerator.generateKeys();

        if (!new File(KEYS_DIR).mkdir()) {
            throw new IOException("Cannot create dir!");
        }

        RSAKeysGenerator.saveToFile(PUBLIC_KEY_FROM_PAIR_PATH, RSAKeysGenerator.getPublicKey().getEncoded());
        RSAKeysGenerator.saveToFile(PRIVATE_KEY_FROM_PAIR_PATH, RSAKeysGenerator.getPrivateKey().getEncoded());

        RSAKeysGenerator.generateKeys();

        RSAKeysGenerator.saveToFile(PRIVATE_KEY_NOT_FROM_PAIR_PATH, RSAKeysGenerator.getPrivateKey().getEncoded());
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.walk(Paths.get(KEYS_DIR))
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
    }

    @Test
    void signVerify_SignAndVerifyCustomObject_Succeed() throws InvalidKeySpecException, SignatureException,
                                                               NoSuchAlgorithmException, InvalidKeyException,
                                                               IOException {
        var testObject = new TestObject("Lorem ipsum dolor sit", 2020);

        var signature = ObjectSignature.sign(BytesConverter.toBytes(testObject),
                                           ObjectSignature.getPrivateKey(PRIVATE_KEY_FROM_PAIR_PATH));

        Assertions.assertTrue(ObjectSignature.verify(BytesConverter.toBytes(testObject), signature,
                                                   ObjectSignature.getPublicKey(PUBLIC_KEY_FROM_PAIR_PATH)));
    }

    @Test
    void signVerify_SignAndVerifyCustomObject_Failed() throws InvalidKeySpecException, SignatureException,
                                                              NoSuchAlgorithmException, InvalidKeyException,
                                                              IOException {
        var testObject = new TestObject("Lorem ipsum dolor sit", 2020);

        var signature = ObjectSignature.sign(BytesConverter.toBytes(testObject),
                                           ObjectSignature.getPrivateKey(PRIVATE_KEY_NOT_FROM_PAIR_PATH));

        Assertions.assertFalse(ObjectSignature.verify(BytesConverter.toBytes(testObject), signature,
                               ObjectSignature.getPublicKey(PUBLIC_KEY_FROM_PAIR_PATH)));
    }

    private static class TestObject implements Serializable {
        String text;
        int number;

        TestObject(String text, int number) {
            this.text = text;
            this.number = number;
        }
    }
}