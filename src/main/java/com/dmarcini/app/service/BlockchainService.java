package com.dmarcini.app.service;

import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.blockchain.TransactionPool;
import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.resources.Resources;
import com.dmarcini.app.reward.Cryptocurrency;
import com.dmarcini.app.users.Client;
import com.dmarcini.app.users.Miner;
import com.dmarcini.app.users.User;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BlockchainService {
    private final static String[] MINERS_NAMES = new String[]{
            "Tom", "Mary", "Lena", "Peter", "Jennifer"
    };

    private final static String[] CLIENTS_NAMES = new String[] {
            "Albert", "Nathalie", "Alex", "Alexandra", "Nathan"
    };

    private final Blockchain blockchain;
    private final User[] miners;
    private final User[] clients;

    public Blockchain getBlockchain() {
        return blockchain;
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

    public static NeedMinersNum builder() {
        return new Builder();
    }

    private BlockchainService(int minersNum, int clientsNum, int startDifficultLevel, int maxNumBlockToMining,
                             int rewardAmount, Cryptocurrency cryptocurrency, int startMinersAmount,
                             int startClientsAmount) throws NoSuchAlgorithmException, NegativeAmountException {
        this.blockchain = new Blockchain(startDifficultLevel,
                new Resources(cryptocurrency, rewardAmount),
                maxNumBlockToMining);
        this.miners = new User[minersNum];
        this.clients = new User[clientsNum];

        TransactionPool transactionPool = new TransactionPool(cryptocurrency);

        for (int i = 0; i < miners.length; ++i) {
            miners[i] = new Miner(MINERS_NAMES[i % MINERS_NAMES.length], blockchain,
                    cryptocurrency, transactionPool, startMinersAmount);
        }
        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new Client(CLIENTS_NAMES[i % CLIENTS_NAMES.length], blockchain,
                    cryptocurrency, transactionPool, startClientsAmount);
        }

        List<User> users = Arrays.asList(Stream.concat(Arrays.stream(miners), Arrays.stream(clients))
                .toArray(User[]::new));

        transactionPool.setUsers(users);
    }

    public final static class Builder implements NeedMinersNum, NeedClientsNum, NeedStartDifficultLevel,
                                                 NeedMaxNumBlockToMining, NeedRewardAmount, NeedCryptocurrency,
                                                 NeedStartMinersAmount, NeedStartClientsAmount, CanBeBuild {
        private int minersNum;
        private int clientsNum;
        private int startDifficultLevel;
        private int maxNumBlockToMining;
        private int rewardAmount;
        private Cryptocurrency cryptocurrency;
        private int startMinersAmount;
        private int startClientsAmount;

        @Override
        public Builder minersNum(int minersNum) {
            this.minersNum = minersNum;

            return this;
        }

        @Override
        public Builder clientsNum(int clientsNum) {
            this.clientsNum = clientsNum;

            return this;
        }

        @Override
        public Builder startDifficultLevel(int startDifficultLevel) {
            this.startDifficultLevel = startDifficultLevel;

            return this;
        }

        @Override
        public Builder maxNumBlockToMining(int maxNumBlockToMining) {
            this.maxNumBlockToMining = maxNumBlockToMining;

            return this;
        }

        @Override
        public Builder rewardAmount(int rewardAmount) {
            this.rewardAmount = rewardAmount;

            return this;
        }

        @Override
        public Builder cryptocurrency(Cryptocurrency cryptocurrency) {
            this.cryptocurrency = cryptocurrency;

            return this;
        }

        @Override
        public Builder startMinersAmount(int startMinersAmount) {
            this.startMinersAmount = startMinersAmount;

            return this;
        }

        @Override
        public Builder startClientsAmount(int startClientsAmount) {
            this.startClientsAmount = startClientsAmount;

            return this;
        }

        @Override
        public BlockchainService build() throws NegativeAmountException, NoSuchAlgorithmException {
            return new BlockchainService(minersNum, clientsNum, startDifficultLevel, maxNumBlockToMining,
                                         rewardAmount, cryptocurrency, startMinersAmount, startClientsAmount);
        }
    }

    public interface NeedMinersNum {
        NeedClientsNum minersNum(int minersNum);
    }

    public interface NeedClientsNum {
        NeedStartDifficultLevel clientsNum(int clientsNum);
    }

    public interface NeedStartDifficultLevel {
        NeedMaxNumBlockToMining startDifficultLevel(int startDifficultLevel);
    }

    public interface NeedMaxNumBlockToMining {
        NeedRewardAmount maxNumBlockToMining(int maxNumBlockToMining);
    }

    public interface NeedRewardAmount {
        NeedCryptocurrency rewardAmount(int rewardAmount);
    }

    public interface NeedCryptocurrency {
        NeedStartMinersAmount cryptocurrency(Cryptocurrency cryptocurrency);
    }

    public interface NeedStartMinersAmount {
        NeedStartClientsAmount startMinersAmount(int startMinersAmount);
    }

    public interface NeedStartClientsAmount {
        CanBeBuild startClientsAmount(int startClientsAmount);
    }

    public interface CanBeBuild {
        BlockchainService build() throws NegativeAmountException, NoSuchAlgorithmException;
    }
}
