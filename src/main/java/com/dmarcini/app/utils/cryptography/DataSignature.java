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
                              String path) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException,
                                                  InvalidKeyException, SignatureException {
        var messageDataSignature = Signature.getInstance("SHA1withRSA");

        messageDataSignature.initSign(getPrivateKey(path));
        messageDataSignature.update(data);

        return messageDataSignature.sign();
    }

    public static boolean verify(byte[] data,
                                 byte[] signature,
                                 String path) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException,
                                                     InvalidKeyException, SignatureException {
        var dataSignature = Signature.getInstance("SHA1withRSA");

        dataSignature.initVerify(getPublicKey(path));
        dataSignature.update(data);

        return dataSignature.verify(signature);
    }

    private static PrivateKey getPrivateKey(String path) throws IOException, NoSuchAlgorithmException,
                                                                           InvalidKeySpecException {
        var keysByte = Files.readAllBytes(new File(path).toPath());
        var pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keysByte);
        var keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    private static PublicKey getPublicKey(String path) throws IOException, NoSuchAlgorithmException,
                                                             InvalidKeySpecException {
        var keyBytes = Files.readAllBytes(new File(path).toPath());
        var x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        var keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(x509EncodedKeySpec);
    }
}
