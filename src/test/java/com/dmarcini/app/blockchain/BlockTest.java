package com.dmarcini.app.blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BlockTest {

    @Test
    void IDGenerator_GenerateCorrectId_True() {
        Block block1 = new Block(0, "");
        Block block2 = new Block(0, "");

        Assertions.assertEquals(block1.getId() + 1, block2.getId());
    }
}