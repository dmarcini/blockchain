package com.dmarcini.app.blockchain;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

class BlockchainTest {
    static Blockchain blockchain;

    @BeforeAll
    static void setUp() {
        blockchain = new Blockchain(2);

        blockchain.setLastBlockTimeGeneration(30);
    }

    @AfterEach
    void clear() throws NoSuchFieldException, IllegalAccessException {
        var blocks = Blockchain.class.getDeclaredField("blocks");
        var lastBlockHash = Blockchain.class.getDeclaredField("lastBlockHash");
        var startZerosNum = Blockchain.class.getDeclaredField("startZerosNum");

        blocks.setAccessible(true);
        blocks.set(blockchain, new ArrayList<Block>());

        lastBlockHash.setAccessible(true);
        lastBlockHash.set(blockchain, "0");

        startZerosNum.setAccessible(true);
        startZerosNum.set(blockchain, 2);
    }

    @Test
    void addValidBlock_isValidStartZerosNum_Succeed() {
        Block block = new Block(1, 1, 2, "0", "00A1",1, 1);

        Assertions.assertTrue(blockchain.addBlock(block));
    }

    @Test
    void addValidBlock_isValidStartZerosNum_Failed() {
        Block block = new Block(1, 1, 2, "0", "0A1", 1, 1);

        Assertions.assertFalse(blockchain.addBlock(block));
    }

    @Test
    void addBlock_isValidPrevBlockHash_Succeed() {
        blockchain.addBlock(new Block(1, 1, 2, "0", "00A1", 1, 1));

        Block block = new Block(2, 1, 2, "00A1", "00B1", 1, 1);

        Assertions.assertTrue(blockchain.addBlock(block));
    }

    @Test
    void addValidBlock_isValidPrevBlockHash_Failed() {
        blockchain.addBlock(new Block(1, 1, 2, "0", "00A1", 1, 1));

        Block block = new Block(2, 1, 2, "00D1", "00B1", 1, 1);

        Assertions.assertFalse(blockchain.addBlock(block));
    }

    @Test
    void getBlock_CheckIfBlockExist_Failed() {
        blockchain.addBlock(new Block(1, 1, 2, "0", "00A1", 1, 1));
        blockchain.addBlock(new Block(2, 1, 2, "00A1", "00B1", 1, 1));

        Assertions.assertFalse(blockchain.getBlock(2).isPresent());
    }

    @Test
    void isValid_IsBlockchainValid_True() {
        blockchain.addBlock(new Block(1, 1, 2, "0", "00A1", 1, 1));
        blockchain.addBlock(new Block(2, 1, 2, "00A1", "00B1", 1, 1));
        blockchain.addBlock(new Block(3, 1, 2, "00B1", "00C1", 1, 1));

        Assertions.assertTrue(blockchain.isValidChain());
    }

    @Test
    void isValid_IsBlockchainValid_False() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<Block> blocksList = new ArrayList<>();

        blocksList.add(new Block(1, 1, 2, "0", "00A1", 1, 1));
        blocksList.add(new Block(2, 1, 2, "00D1", "00B1", 1, 1));
        blocksList.add(new Block(3, 1, 2, "00B1", "00C1", 1, 1));

        var blocks = Blockchain.class.getDeclaredField("blocks");

        blocks.setAccessible(true);
        blocks.set(blockchain, blocksList);

        Assertions.assertFalse(blockchain.isValidChain());
    }
}