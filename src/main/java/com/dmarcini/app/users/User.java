package com.dmarcini.app.users;

import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.blockchain.TransactionPool;
import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.resources.Wallet;
import com.dmarcini.app.reward.Cryptocurrency;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public abstract class User implements Serializable, Runnable {
    protected static final Random generator = new Random();

    private static final AtomicLong userIdGenerator = new AtomicLong(1);

    protected final long id;
    protected final String name;
    protected final Blockchain blockchain;
    protected final Wallet wallet;
    protected final TransactionPool transactionPool;

    User(String name, Blockchain blockchain, Cryptocurrency cryptocurrency,
         TransactionPool transactionPool, int startAmount) throws NegativeAmountException {
        this.id = userIdGenerator.getAndIncrement();
        this.name = name;
        this.blockchain = blockchain;
        this.wallet = new Wallet(cryptocurrency);
        this.transactionPool = transactionPool;

        this.wallet.addAmount(startAmount);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public abstract void run();
}
