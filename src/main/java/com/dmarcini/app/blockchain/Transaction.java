package com.dmarcini.app.blockchainsystem;

import com.dmarcini.app.reward.Cryptocurrency;
import com.dmarcini.app.users.User;
import com.dmarcini.app.utils.cryptography.ObjectSignature;

import java.io.IOException;
import java.io.Serializable;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

public final class Transaction implements Serializable {
    private long id;
    private final User from;
    private final User to;
    private final Map<Cryptocurrency, Long> resources;
    private final PublicKey publicKey;
    private byte[] signature;

    public Transaction(User from, User to, Map<Cryptocurrency, Long> resources, PublicKey publicKey) {
        this.from = from;
        this.to = to;
        this.resources = new HashMap<>(resources);
        this.publicKey = publicKey;
    }

    public Transaction(Transaction transaction) {
        this.id = transaction.id;
        this.from = transaction.from;
        this.to = transaction.to;
        this.resources = new HashMap<>(transaction.resources);
        this.publicKey = transaction.publicKey;
        this.signature = transaction.signature.clone();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    public Map<Cryptocurrency, Long> getResources() {
        return resources;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void sign(PrivateKey privateKey) throws IOException, NoSuchAlgorithmException,
                                                   InvalidKeyException, SignatureException {
        signature = ObjectSignature.sign(this, privateKey);
    }
}