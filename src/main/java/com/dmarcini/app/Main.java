package com.dmarcini.app;

import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.users.Client;
import com.dmarcini.app.users.Miner;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private final static int MINERS_NUMBER = 5;
    private final static int START_ZEROS_NUMBER = 0;
    private final static String[] NAMES = new String[] {
            "Tom", "Mary", "Lena", "Peter", "Jennifer"
    };


    private final static Blockchain blockchain = new Blockchain(START_ZEROS_NUMBER);

    public static void start() throws InterruptedException, NoSuchAlgorithmException {
        AtomicBoolean isEnd = new AtomicBoolean(false);

        Thread[] miners = new Thread[MINERS_NUMBER];
        Thread[] clients = new Thread[MINERS_NUMBER];

        for (int i = 0; i < MINERS_NUMBER; ++i) {
            miners[i] = new Thread(new Miner(blockchain, isEnd));
            clients[i] = new Thread(new Client(NAMES[i], blockchain, isEnd));
        }

        for (int i = 0; i < MINERS_NUMBER; ++i) {
            miners[i].start();
            clients[i].start();
        }

        for (int i = 0; i < MINERS_NUMBER; ++i) {
            miners[i].join();
            clients[i].join();
        }
    }

    public static void main(final String[] args) throws InterruptedException, NoSuchAlgorithmException {
        start();

        System.out.println(blockchain);
    }
}
