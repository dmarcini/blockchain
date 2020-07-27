package com.dmarcini.app.blockchain;

import com.dmarcini.app.utils.BytesConverter;
import com.dmarcini.app.utils.cryptography.DataSignature;

import java.io.IOException;
import java.io.Serializable;
import java.security.*;

public class Message implements Serializable {
    private long id;
    private final String client;
    private final String text;
    private final PublicKey publicKey;
    private byte[] signature;

    public Message(String client, String text,
                   PublicKey publicKey) {
        this.client = client;
        this.text = text;
        this.publicKey = publicKey;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getClient() {
        return client;
    }

    public String getText() {
        return text;
    }

    public void sign(PrivateKey privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException,
                                                     SignatureException {
        signature = DataSignature.sign(BytesConverter.toBytes(this), privateKey);
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
