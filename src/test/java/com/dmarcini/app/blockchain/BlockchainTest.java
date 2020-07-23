package com.dmarcini.app.blockchain;

import org.junit.jupiter.api.*;

class BlockchainTest {
    static Blockchain blockchain;

    @BeforeAll
    static void setUp() {
        blockchain = new Blockchain(0);
    }

    @AfterEach
    void clear() {
        blockchain.removeAllBlocks();
    }

    @Test
    void generateBlocks_Generate10Blocks_Succeed() {
        blockchain.generateBlocks(10);

        Assertions.assertEquals(10, blockchain.getSize());
    }

    @Test
    void generateBlocks_GenerateLessThan0Blocks_Failed() {
        blockchain.generateBlocks(-10);

        Assertions.assertEquals(0, blockchain.getSize());
    }

    @Test
    void removeAllBlocks_RemoveAllBlocks_Succeed() {
        blockchain.generateBlocks(10);
        blockchain.removeAllBlocks();

        Assertions.assertEquals(0, blockchain.getSize());
    }

    @Test
    void getSize_GetSizeBlockchainConsisting10Blocks_Succeed() {
        blockchain.generateBlocks(10);

        Assertions.assertEquals(10, blockchain.getSize());
    }

    @Test
    void getBlock_CheckIfBlockExist_Failed() {
        blockchain.generateBlocks(5);

        Assertions.assertFalse(blockchain.getBlock(5).isPresent());
    }

    @Test
    void addBlock_Add3Blocks_Succeed() {
        blockchain.addBlock();
        blockchain.addBlock();
        blockchain.addBlock();

        Assertions.assertEquals(3, blockchain.getSize());
    }

    @Test
    void isValid_IsBlockchainValid_True() {
        blockchain.generateBlocks(3);

        Assertions.assertTrue(blockchain.isValid());
    }
}