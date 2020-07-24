package com.dmarcini.app;

import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.miner.Miner;

public class Main {
    private final static int MINERS_NUMBER = 5;
    private final static int START_ZEROS_NUMBER = 0;

    final static Blockchain blockchain = new Blockchain(START_ZEROS_NUMBER);

    public static void mine() throws InterruptedException {
        Thread[] threads = new Thread[MINERS_NUMBER];

        for (int i = 0; i < MINERS_NUMBER; ++i) {
            threads[i] = new Thread(new Miner(blockchain));
        }

        for (int i = 0; i < MINERS_NUMBER; ++i) {
            threads[i].start();
        }

        for (int i = 0; i < MINERS_NUMBER; ++i) {
            threads[i].join();
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        mine();

        System.out.println(blockchain);
    }
}
