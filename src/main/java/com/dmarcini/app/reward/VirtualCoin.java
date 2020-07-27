package com.dmarcini.app.reward;

import java.io.Serializable;

public class VirtualCoin implements Cryptocurrency, Serializable {
    private final String currency;
    private final int amount;

    public VirtualCoin(int amount) {
        this.currency = "VC";
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public String getCurrency() {
        return currency;
    }
}
