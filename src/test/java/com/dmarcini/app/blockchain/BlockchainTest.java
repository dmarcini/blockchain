package com.dmarcini.app.blockchainsystem;

import java.security.*;

class BlockchainTest {
/*    private final static Blockchain blockchain = new Blockchain(2);
    private final static long timeGeneration = 30;

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException {
        var keysGenerator = new RSAKeysGenerator(1024);

        keysGenerator.generateKeys();

        privateKey = keysGenerator.getPrivateKey();
        publicKey = keysGenerator.getPublicKey();
    }

    @AfterEach
    void clear() throws NoSuchFieldException, IllegalAccessException {
        var blocks = Blockchain.class.getDeclaredField("blocks");

        blocks.setAccessible(true);
        blocks.set(blockchain, new ArrayList<Block>());
    }

    @Test
    void addValidBlock_IsValidStartZerosNum_Succeed() {
        Assertions.assertTrue(blockchain.addBlock(new Block(1, "0", "00A1", timeGeneration), 1));
    }

    @Test
    void addValidBlock_IsValidStartZerosNum_Failed() {
        Assertions.assertFalse(blockchain.addBlock(new Block(1, "0", "0A1", timeGeneration), 1));
    }

    @Test
    void addBlock_IsValidPrevBlockHash_Succeed() {
        blockchain.addBlock(new Block(1, "0", "00A1", timeGeneration), 1);

        Assertions.assertTrue(blockchain.addBlock(new Block(2, "00A1", "00B1", timeGeneration), 1));
    }

    @Test
    void addValidBlock_IsValidPrevBlockHash_Failed() {
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
/*
    @Test
    void isValid_AreValidMessagesId_True() throws NoSuchAlgorithmException, SignatureException,
                                                  InvalidKeyException, IOException {
        blockchain.addMessage(new Transaction("", "", publicKey), privateKey);
        blockchain.addMessage(new Transaction("", "", publicKey), privateKey);
        blockchain.addMessage(new Transaction("", "", publicKey), privateKey);

        blockchain.addBlock(new Block(1, "0", "00A1", 30), 1);

        blockchain.addMessage(new Transaction("", "", publicKey), privateKey);
        blockchain.addMessage(new Transaction("", "", publicKey), privateKey);

        blockchain.addBlock(new Block(2, "00A1", "00B1", 30), 1);

        Assertions.assertTrue(blockchain.isValidChain());
    }

    @Test
    void isValid_AreValidMessagesId_False() throws NoSuchFieldException, IllegalAccessException {
        Transaction msg1 = new Transaction("", "", publicKey);
        Transaction msg2 = new Transaction("", "", publicKey);
        Transaction msg3 = new Transaction("", "", publicKey);

        msg1.setId(3);
        msg2.setId(1);
        msg3.setId(2);

        ArrayList<Transaction> messagesList = new ArrayList<>(Arrays.asList(msg1, msg2, msg3));

        var messages = Blockchain.class.getDeclaredField("messages");

        messages.setAccessible(true);
        messages.set(blockchain, messagesList);

        blockchain.addBlock(new Block(1, "0", "00A1", 30), 1);

        Assertions.assertFalse(blockchain.isValidChain());
    }*/
}