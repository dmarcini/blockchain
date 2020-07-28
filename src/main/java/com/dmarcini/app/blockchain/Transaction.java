package com.dmarcini.app.blockchain;

import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.resources.Resources;
import com.dmarcini.app.users.User;
import com.dmarcini.app.utils.cryptography.ObjectSignature;

import java.io.IOException;
import java.io.Serializable;
import java.security.*;

public final class Transaction implements Serializable {
    private long id;
    private final User from;
    private final User to;
    private final Resources resources;
    private final PublicKey publicKey;
    private byte[] signature;

    public Transaction(User from, User to, Resources resources, PublicKey publicKey) {
        this.from = from;
        this.to = to;
        this.resources = new Resources(resources);
        this.publicKey = publicKey;
    }

    public Transaction(Transaction transaction) {
        this.id = transaction.id;
        this.from = transaction.from;
        this.to = transaction.to;
        this.resources = new Resources(transaction.resources);
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

    public Resources getResources() {
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

    public void execute() {
        try {
            from.getWallet().subtractAmount(resources.getAmount());
            to.getWallet().addAmount(resources.getAmount());
        } catch(NegativeAmountException ignored) {}
    }

    @Override
    public String toString() {
        return "Transaction id = " + id + "\n" +
               "From: " + from.getName() + "\n" +
               "To: " + to.getName() + "\n" +
               "Resources: " + resources.getAmount() + resources.getCryptocurrency().getCurrency() + "\n";
    }
}