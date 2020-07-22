package com.dmarcini.app.blockchain;

import org.junit.jupiter.api.*;

class BlockchainTest {
    static Blockchain blockchain;

    @BeforeAll
    static void setUp() {
        blockchain = new Blockchain();
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
    void generateBlocks_PreviousBlockHashEqualsHashPreviousBlock_True() {
        blockchain.generateBlocks(2);

        String firstBlockHash = blockchain.getBlock(0)
                                          .getHash();
        String previousBlockHashInSecondBlock = blockchain.getBlock(1)
                                                          .getPreviousBlockHash();

        Assertions.assertEquals(firstBlockHash, previousBlockHashInSecondBlock);
    }

    @Test
    void getBlock_GetBlockFromOutsideBlockchain_ThrowsIndexOutOfBoundsException() {
        blockchain.generateBlocks(5);

        Assertions.assertThrows(IndexOutOfBoundsException.class,
                                () -> blockchain.getBlock(5));
    }
}