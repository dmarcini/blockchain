package com.dmarcini.app.resources;

import com.dmarcini.app.reward.VirtualCoin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourcesTest {
    @Test
    public void addAmount_AddPositiveAmount_Succeed() throws NegativeAmountException {
        Resources resources = new Resources(new VirtualCoin());

        resources.addAmount(50);

        Assertions.assertEquals(50, resources.getAmount());
    }

    @Test
    public void addAmount_AddNegativeAmount_ThrowsNegativeAmountException() {
        Resources resources = new Resources(new VirtualCoin());

        Assertions.assertThrows(NegativeAmountException.class, () -> resources.addAmount(-50));
    }

    @Test
    public void subtractAmount_SubtractPositiveAmount_Succeed() throws NegativeAmountException {
        Resources resources = new Resources(new VirtualCoin(), 100);

        resources.subtractAmount(50);

        Assertions.assertEquals(50, resources.getAmount());
    }

    @Test
    public void subtractAmount_SubtractNegativeAmount_ThrowsNegativeAmountException() {
        Resources resources = new Resources(new VirtualCoin());

        Assertions.assertThrows(NegativeAmountException.class, () -> resources.subtractAmount(-50));
    }

    @Test
    public void subtractAmount_NegativeAmountResult_ThrowsNegativeAmountException() {
        Resources resources = new Resources(new VirtualCoin(), 0);

        Assertions.assertThrows(NegativeAmountException.class, () -> resources.subtractAmount(50));
    }
}
