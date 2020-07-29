package com.dmarcini.app.resources;

import com.dmarcini.app.reward.Cryptocurrency;

import java.io.Serializable;

public class Resources implements Serializable {
    private final Cryptocurrency cryptocurrency;
    private int amount;

    public Resources(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
        this.amount = 0;
    }

    public Resources(Cryptocurrency cryptocurrency, int amount) {
        this.cryptocurrency = cryptocurrency;
        this.amount = amount;
    }

    public Resources(Resources resources) {
        this.cryptocurrency = resources.cryptocurrency;
        this.amount = resources.amount;
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Amount can't be negative!");
        }

        this.amount += amount;
    }

    public void subtractAmount(int amount) throws NegativeAmountException {
        if (amount < 0 || amount > this.amount) {
            throw new NegativeAmountException("Amount can't be negative!");
        }

        this.amount -= amount;
    }

    @Override
    public String toString() {
        return amount + cryptocurrency.getCurrency();
    }

}
