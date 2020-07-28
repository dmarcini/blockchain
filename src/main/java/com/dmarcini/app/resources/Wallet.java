package com.dmarcini.app.resources;

import com.dmarcini.app.reward.Cryptocurrency;

import java.io.Serializable;

public class Wallet implements Serializable {
    private final Resources resources;

    public Wallet(Cryptocurrency cryptocurrency) {
        this.resources = new Resources(cryptocurrency);
    }

    public Resources getResources() {
        return resources;
    }

    public void addAmount(int amount) {
        resources.addAmount(amount);
    }

    public void subtractAmount(int amount) {
        if (resources.getAmount() >= amount) {
            resources.subtractAmount(amount);
        }
    }

    @Override
    public String toString() {
        return resources.getAmount() + resources.getCryptocurrency().getCurrency();
    }
}
