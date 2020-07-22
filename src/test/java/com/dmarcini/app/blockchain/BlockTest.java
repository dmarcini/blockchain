package com.dmarcini.app.blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BlockTest {

    @Test
    void IDGenerator_GenerateCorrectId_True() {
        Block block1 = new Block("");
        Block block2 = new Block("");

        Assertions.assertNotEquals(block1.getId(), block2.getId());
    }
}