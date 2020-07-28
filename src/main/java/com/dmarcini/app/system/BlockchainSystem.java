package com.dmarcini.app.system;

import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.blockchain.TransactionPool;
import com.dmarcini.app.resources.Resources;
import com.dmarcini.app.reward.Cryptocurrency;
import com.dmarcini.app.users.Client;
import com.dmarcini.app.users.Miner;
import com.dmarcini.app.users.User;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BlockchainSystem {
    private final static String[] MINERS_NAMES = new String[]{
            "Tom", "Mary", "Lena", "Peter", "Jennifer"
    };

    private final static String[] CLIENTS_NAMES = new String[] {
            "Albert", "Nathalie", "Alex", "Alexandra", "Nathan"
    };

    private final TransactionPool transactionPool;
    private final Blockchain blockchain;
    private final User[] miners;
    private final User[] clients;

    public BlockchainSystem(int minersNum, int clientsNum, int startDifficultLevel,
                            int maxNumBlockToMining, int rewardAmount,
                            Cryptocurrency cryptocurrency) throws NoSuchAlgorithmException {
        this.transactionPool = new TransactionPool(cryptocurrency);
        this.blockchain = new Blockchain(startDifficultLevel,
                                         new Resources(cryptocurrency, rewardAmount),
                                         maxNumBlockToMining);
        this.miners = new User[minersNum];
        this.clients = new User[clientsNum];

        for (int i = 0; i < miners.length; ++i) {
            miners[i] = new Miner(MINERS_NAMES[i % MINERS_NAMES.length],
                                  blockchain, cryptocurrency, transactionPool);
        }
        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new Client(CLIENTS_NAMES[i % CLIENTS_NAMES.length],
                                    blockchain, cryptocurrency, transactionPool);
        }

        List<User> users = Arrays.asList(Stream.concat(Arrays.stream(miners), Arrays.stream(clients))
                                               .toArray(User[]::new));

        transactionPool.setUsers(users);
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public User[] getMiners() {
        return miners;
    }

    public User[] getClients() {
        return clients;
    }

    public void start() throws InterruptedException {
        final Thread[] minerThreads = new Thread[miners.length];
        final Thread[] clientThreads = new Thread[clients.length];

        for (int i = 0; i < miners.length; ++i) {
            minerThreads[i] = new Thread(miners[i]);
        }
        for (int i = 0; i < clients.length; ++i) {
            clientThreads[i] = new Thread(clients[i]);
        }

        for (Thread minerThread : minerThreads) {
            minerThread.start();
        }
        for (Thread clientThread : clientThreads) {
            clientThread.start();
        }

        for (Thread minerThread : minerThreads) {
            minerThread.join();
        }
        for (Thread clientThread : clientThreads) {
            clientThread.join();
        }
    }
}
