package com.dmarcini.app.blockchain;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

class BlockchainTest {
    private final static Blockchain blockchain = new Blockchain(2);
    private final static long timeGeneration = 30;

    @AfterEach
    void clear() throws NoSuchFieldException, IllegalAccessException {
        var blocks = Blockchain.class.getDeclaredField("blocks");

        blocks.setAccessible(true);
        blocks.set(blockchain, new ArrayList<Block>());
    }

    @Test
    void addValidBlock_isValidStartZerosNum_Succeed() {
        Assertions.assertTrue(blockchain.addBlock(new Block(1, "0", "00A1", timeGeneration), 1));
    }

    @Test
    void addValidBlock_isValidStartZerosNum_Failed() {
        Assertions.assertFalse(blockchain.addBlock(new Block(1, "0", "0A1", timeGeneration), 1));
    }

    @Test
    void addBlock_isValidPrevBlockHash_Succeed() {
        blockchain.addBlock(new Block(1, "0", "00A1", timeGeneration), 1);

        Assertions.assertTrue(blockchain.addBlock(new Block(2, "00A1", "00B1", timeGeneration), 1));
    }

    @Test
    void addValidBlock_isValidPrevBlockHash_Failed() {
        blockchain.addBlock(new Block(1, "0", "00A1", timeGeneration), 1);

        Assertions.assertFalse(blockchain.addBlock(new Block(2, "00D1", "00B1", timeGeneration), 1));
    }

    @Test
    void getBlock_CheckIfBlockExist_Failed() {
        blockchain.addBlock(new Block(1, "0", "00A1", timeGeneration), 1);
        blockchain.addBlock(new Block(2, "00A1", "00B1", timeGeneration), 1);

        Assertions.assertFalse(blockchain.getBlock(2).isPresent());
    }

    @Test
    void isValid_IsBlockchainValid_True() {
        blockchain.addBlock(new Block(1, "0", "00A1", timeGeneration), 1);
        blockchain.addBlock(new Block(2, "00A1", "00B1", timeGeneration), 1);
        blockchain.addBlock(new Block(3, "00B1", "00C1", timeGeneration), 1);

        Assertions.assertTrue(blockchain.isValidChain());
    }

    @Test
    void isValid_IsBlockchainValid_False() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<Block> blocksList = new ArrayList<>();

        blocksList.add(new Block(1, "0", "00A1", timeGeneration));
        blocksList.add(new Block(2, "00D1", "00B1", timeGeneration));
        blocksList.add(new Block(3, "00B1", "00C1", timeGeneration));

        var blocks = Blockchain.class.getDeclaredField("blocks");

        blocks.setAccessible(true);
        blocks.set(blockchain, blocksList);

        Assertions.assertFalse(blockchain.isValidChain());
    }
}