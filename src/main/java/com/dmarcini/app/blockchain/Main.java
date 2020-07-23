package com.dmarcini.app.blockchain;

import java.util.Scanner;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(final String[] args) {
        System.out.println("Enter how many zeros the hash must start with: ");
        int startZerosNum = scanner.nextInt();

        Blockchain blockchain = new Blockchain(startZerosNum);

        blockchain.generateBlocks(5);

        System.out.println(blockchain);
    }
}
