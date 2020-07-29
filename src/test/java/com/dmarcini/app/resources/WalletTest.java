package com.dmarcini.app.resources;

import com.dmarcini.app.reward.VirtualCoin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WalletTest {
    @Test
    public void addAmount_AddPositiveAmount_Succeed() throws NegativeAmountException {
        Wallet wallet = new Wallet(new VirtualCoin());

        wallet.addAmount(50);

        Assertions.assertEquals(50, wallet.getResources().getAmount());
    }

    @Test
    public void addAmount_AddNegativeAmount_ThrowsNegativeAmountException() {
        Wallet wallet = new Wallet(new VirtualCoin());

        Assertions.assertThrows(NegativeAmountException.class, () -> wallet.addAmount(-50));
    }

    @Test
    public void subtractAmount_SubtractPositiveAmount_Succeed() throws NegativeAmountException {
        Wallet wallet = new Wallet(new VirtualCoin());

        wallet.addAmount(50);
        wallet.subtractAmount(50);

        Assertions.assertEquals(0, wallet.getResources().getAmount());
    }

    @Test
    public void subtractAmount_SubtractNegativeAmount_ThrowsNegativeAmountException() {
        Wallet wallet = new Wallet(new VirtualCoin());

        Assertions.assertThrows(NegativeAmountException.class, () -> wallet.subtractAmount(-50));
    }

    @Test
    public void subtractAmount_NegativeAmountResult_ThrowsNegativeAmountException() {
        Wallet wallet = new Wallet(new VirtualCoin());

        Assertions.assertThrows(NegativeAmountException.class, () -> wallet.subtractAmount(50));
    }
}
