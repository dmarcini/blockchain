package com.dmarcini.app.utils.cryptography;

import com.dmarcini.app.utils.BytesConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ObjectSignature {
    public static <T> byte[] sign(T object,
                                  PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException,
                                                                SignatureException, IOException {
        var signature = Signature.getInstance("SHA1withRSA");

        signature.initSign(privateKey);
        signature.update(BytesConverter.toBytes(object));

        return signature.sign();
    }

    public static <T> boolean verify(T object,
                                     byte[] objectSignature,
                                     PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException,
                                                                 SignatureException, IOException {
        var signature = Signature.getInstance("SHA1withRSA");

        signature.initVerify(publicKey);
        signature.update(BytesConverter.toBytes(object));

        return signature.verify(objectSignature);
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
