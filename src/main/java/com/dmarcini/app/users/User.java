package com.dmarcini.app.users;

import com.dmarcini.app.blockchainsystem.Blockchain;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class User implements Runnable {
    protected final long id;
    protected final String name;
    protected final Blockchain blockchain;
    protected final AtomicBoolean isEnd;

    User(long id, String name, Blockchain blockchain, AtomicBoolean isEnd) {
        this.id = id;
        this.name = name;
        this.blockchain = blockchain;
        this.isEnd = isEnd;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public abstract void run();
}
