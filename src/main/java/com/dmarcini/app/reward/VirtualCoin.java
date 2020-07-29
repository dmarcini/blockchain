package com.dmarcini.app.reward;

import java.io.Serializable;

public final class VirtualCoin implements Cryptocurrency, Serializable {
    private final String currency;
    private final int value;

    public VirtualCoin() {
        this.currency = "VC";
        this.value = 1;
    }

    public VirtualCoin(int value) {
        this.currency = "VC";
        this.value = value;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public int getValue() {
        return value;
    }
}
