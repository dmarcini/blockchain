package com.dmarcini.app.blockchain;

public class Main {
    public static void main(final String[] args) {
        Blockchain blockchain = new Blockchain();

        blockchain.generateBlocks(10);

        System.out.println(blockchain);
    }
}
