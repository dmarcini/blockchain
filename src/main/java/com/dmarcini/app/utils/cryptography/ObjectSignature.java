package com.dmarcini.app.utils.cryptography;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class DataSignature {
    public static byte[] sign(byte[] data,
                              PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException,
                                                            SignatureException {
        var messageDataSignature = Signature.getInstance("SHA1withRSA");

        messageDataSignature.initSign(privateKey);
        messageDataSignature.update(data);

        return messageDataSignature.sign();
    }

    public static boolean verify(byte[] data,
                                 byte[] signature,
                                 PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException,
                                                             SignatureException {
        var dataSignature = Signature.getInstance("SHA1withRSA");

        dataSignature.initVerify(publicKey);
        dataSignature.update(data);

        return dataSignature.verify(signature);
    }

    public static PublicKey getPublicKey(String path) throws IOException, NoSuchAlgorithmException,
                                                             InvalidKeySpecException {
        var keyBytes = Files.readAllBytes(new File(path).toPath());
        var x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        var keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    public static PrivateKey getPrivateKey(String path) throws IOException, NoSuchAlgorithmException,
                                                               InvalidKeySpecException {
        var keysByte = Files.readAllBytes(new File(path).toPath());
        var pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keysByte);
        var keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }
}
