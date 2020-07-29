package com.dmarcini.app.blockchain;

import com.dmarcini.app.reward.Cryptocurrency;
import com.dmarcini.app.users.User;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public final class TransactionPool implements Serializable {
    private List<User> users;
    private final Cryptocurrency cryptocurrency;

    public TransactionPool(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getUsersNum() {
        return users.size();
    }

    public Optional<User> getUser(int id) {
        return (id >= 0 && id < users.size()) ? Optional.of(users.get(id)) : Optional.empty();
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }
}
