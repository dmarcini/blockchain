package com.dmarcini.app.users;

import com.dmarcini.app.blockchainsystem.Blockchain;
import com.dmarcini.app.utils.cryptography.RSAKeysGenerator;

import java.security.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Client extends User {
    private static final int KEY_LENGTH = 1024;

    private static final AtomicLong clientIdGenerator = new AtomicLong(1);

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public Client(String name, Blockchain blockchain, AtomicBoolean isEnd) throws NoSuchAlgorithmException {
        super(clientIdGenerator.getAndIncrement(), name, blockchain, isEnd);

        RSAKeysGenerator RSAKeysGenerator = new RSAKeysGenerator(KEY_LENGTH);

        RSAKeysGenerator.generateKeys();

        this.privateKey = RSAKeysGenerator.getPrivateKey();
        this.publicKey = RSAKeysGenerator.getPublicKey();
    }

    @Override
    public void run() {
        while (!isEnd.get()) {

        }
    }
}
