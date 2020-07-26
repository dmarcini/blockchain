package com.dmarcini.app.users;

import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.blockchain.Data;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements Runnable {
    private static final String[] NAMES = new String[] {
            "Tom", "Marry", "Victor", "Jane", "Elizabeth", "Peter"
    };

    private final long id;
    private final String name;
    private final Blockchain blockchain;
    private final AtomicBoolean isEnd;

    static final Scanner scanner = new Scanner(System.in);

    public Client(Blockchain blockchain, AtomicBoolean isEnd) {
        this.id = ClientIDGenerator.getId();
        this.name = NAMES[(int) (id % NAMES.length)];
        this.blockchain = blockchain;
        this.isEnd = isEnd;
    }

    public Client(String name, Blockchain blockchain, AtomicBoolean isEnd) {
        this.id = ClientIDGenerator.getId();
        this.name = name;
        this.blockchain = blockchain;
        this.isEnd = isEnd;
    }

    public void addMessage() {
        String message;

        System.out.println(name + ": ");

        synchronized (scanner) {
            message = scanner.nextLine();
        }

        synchronized (blockchain) {
            blockchain.addData(new Data(name, message));
        }
    }

    @Override
    public void run() {
        while (!isEnd.get()) {
            addMessage();
        }
    }

    private static class ClientIDGenerator {
        private static long id = 1;

        public static long getId() {
            return id++;
        }
    }
}
