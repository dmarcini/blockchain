package com.dmarcini.app.users;

import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.blockchain.Message;
import com.dmarcini.app.utils.cryptography.KeysGenerator;

import java.io.IOException;
import java.security.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Client implements Runnable {
    private final static AtomicLong clientIdGenerator = new AtomicLong(1);

    private final long id;
    private final String name;
    private final Blockchain blockchain;
    private final AtomicBoolean isEnd;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    static final Scanner scanner = new Scanner(System.in);

    public Client(String name, Blockchain blockchain, AtomicBoolean isEnd) throws NoSuchAlgorithmException {
        this.id = clientIdGenerator.getAndIncrement();
        this.name = name;
        this.blockchain = blockchain;
        this.isEnd = isEnd;

        KeysGenerator keysGenerator = new KeysGenerator(1024);

        keysGenerator.generateKeys();

        this.privateKey = keysGenerator.getPrivateKey();
        this.publicKey = keysGenerator.getPublicKey();
    }

    public void addMessage() throws IOException, NoSuchAlgorithmException,
                                    InvalidKeyException, SignatureException {
        String text;

        System.out.println(name + ": ");

        synchronized (scanner) {
            text = scanner.nextLine();
        }

        synchronized (blockchain) {
            Message message = new Message(name, text, publicKey);

            blockchain.addMessage(message, privateKey);
        }
    }

    @Override
    public void run() {
        while (!isEnd.get()) {
            try {
                addMessage();
            } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                e.printStackTrace();
            }
        }
    }
}
