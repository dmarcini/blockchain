package com.dmarcini.app.blockchain;

import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.resources.Resources;
import com.dmarcini.app.reward.VirtualCoin;
import com.dmarcini.app.users.Client;
import com.dmarcini.app.users.Miner;
import com.dmarcini.app.users.User;
import com.dmarcini.app.utils.cryptography.RSAKeysGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.*;
import java.util.*;

class BlockchainTest {
    private static final Resources reward = new Resources(new VirtualCoin(), 100);
    private static final TransactionPool transactionPool = new TransactionPool(new VirtualCoin());

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException {
        var keysGenerator = new RSAKeysGenerator(1024);

        keysGenerator.generateKeys();

        privateKey = keysGenerator.getPrivateKey();
        publicKey = keysGenerator.getPublicKey();
    }

    @Test
    void addValidBlock_IsValidDifficultLevel_Succeed() throws NegativeAmountException {
        Blockchain blockchain = new Blockchain(2, reward, 1);

        User miner = new Miner("Damian", blockchain, new VirtualCoin(), transactionPool);
        Block block = new Block(1, 1, "0");

        block.setHash("00A1");

        Assertions.assertTrue(blockchain.addBlock(block, miner));
    }

    @Test
    void addValidBlock_IsValidDifficultLevel_Failed() throws NegativeAmountException {
        Blockchain blockchain = new Blockchain(2, reward, 1);

        User miner = new Miner("Damian", blockchain, new VirtualCoin(), transactionPool);
        Block block = new Block(1, 1, "0");

        block.setHash("0A1");

        Assertions.assertFalse(blockchain.addBlock(block, miner));
    }

    @Test
    void addBlock_IsValidPrevBlockHash_Succeed() throws NegativeAmountException {
        Blockchain blockchain = new Blockchain(2, reward, 1);

        User miner = new Miner("Damian", blockchain, new VirtualCoin(), transactionPool);
        Block block = new Block(1, 1, "0");

        block.setHash("00A1");

        blockchain.addBlock(block, miner);

        block = new Block(2, 1, "00A1");

        block.setHash("00B1");

        Assertions.assertTrue(blockchain.addBlock(block, miner));
    }

    @Test
    void addBlock_IsValidPrevBlockHash_Failed() throws NegativeAmountException {
        Blockchain blockchain = new Blockchain(2, reward, 1);

        User miner = new Miner("Damian", blockchain, new VirtualCoin(), transactionPool);
        Block block = new Block(1, 1, "0");

        block.setHash("00A1");

        blockchain.addBlock(block, miner);

        block = new Block(2, 1, "00D1");

        block.setHash("00B1");

        Assertions.assertFalse(blockchain.addBlock(block, miner));
    }


    @Test
    void isValid_ArePrevHashesValid_True() throws NegativeAmountException {
        Blockchain blockchain = new Blockchain(2, reward, 1);

        User miner = new Miner("Damian", blockchain, new VirtualCoin(), transactionPool);
        Block block1 = new Block(1, 1, "0");
        Block block2 = new Block(2, 1, "00A1");
        Block block3 = new Block(3, 1, "00B1");

        block1.setHash("00A1");
        block2.setHash("00B1");
        block3.setHash("00C1");

        blockchain.addBlock(block1, miner);
        blockchain.addBlock(block2, miner);
        blockchain.addBlock(block3, miner);

        Assertions.assertTrue(blockchain.isValidChain());
    }

    @Test
    void isValid_ArePrevHashesValid_False() throws NoSuchFieldException, IllegalAccessException {
        Blockchain blockchain = new Blockchain(2, reward, 1);

        List<Block> blocksList = new ArrayList<>();

        blocksList.add(new Block(1, 1, "0"));
        blocksList.add(new Block(2, 1, "00D1"));
        blocksList.add(new Block(3, 1, "00B1"));

        var blocks = Blockchain.class.getDeclaredField("blockchain");

        blocks.setAccessible(true);
        blocks.set(blockchain, blocksList);

        Assertions.assertFalse(blockchain.isValidChain());
    }

    @Test
    void isValid_AreTransactionsValid_True() throws NoSuchAlgorithmException, NegativeAmountException,
                                                    IOException, SignatureException, InvalidKeyException {
        Blockchain blockchain = new Blockchain(0, new Resources(new VirtualCoin(), 100), 5);
        TransactionPool transactionPool = new TransactionPool(new VirtualCoin());

        User from = new Client("Mary", blockchain, new VirtualCoin(), transactionPool);
        User to = new Client("Henry", blockchain, new VirtualCoin(), transactionPool);

        Resources resources = new Resources(new VirtualCoin(), 1);

        from.getWallet().addAmount(100);
        to.getWallet().addAmount(100);

        blockchain.addTransaction(new Transaction(from ,to, resources, publicKey), privateKey);
        blockchain.addTransaction(new Transaction(to, from, resources, publicKey), privateKey);
        blockchain.addTransaction(new Transaction(from ,to, resources, publicKey), privateKey);

        Block block1 = new Block(1, 1, "0");
        Block block2 = new Block(2, 1, "00A1");

        block1.setHash("00A1");
        block2.setHash("00B1");

        blockchain.addBlock(block1, from);

        blockchain.addTransaction(new Transaction(to, from, resources, publicKey), privateKey);
        blockchain.addTransaction(new Transaction(from ,to, resources, publicKey), privateKey);

        blockchain.addBlock(block2, to);

        Assertions.assertTrue(blockchain.isValidChain());
    }

    @Test
    void isValid_AreTransactionsValid_False() throws NoSuchAlgorithmException, NegativeAmountException,
                                                     IOException, SignatureException, InvalidKeyException,
                                                     NoSuchFieldException, IllegalAccessException {
        Blockchain blockchain = new Blockchain(0, new Resources(new VirtualCoin(), 100), 5);
        TransactionPool transactionPool = new TransactionPool(new VirtualCoin());

        User from = new Client("Mary", blockchain, new VirtualCoin(), transactionPool);
        User to = new Client("Henry", blockchain, new VirtualCoin(), transactionPool);

        Resources resources = new Resources(new VirtualCoin(), 1);

        from.getWallet().addAmount(100);
        to.getWallet().addAmount(100);

        Transaction transaction1 = new Transaction(from ,to, resources, publicKey);
        Transaction transaction2 = new Transaction(to, from, resources, publicKey);
        Transaction transaction3 = new Transaction(from ,to, resources, publicKey);

        transaction1.setId(4);
        transaction2.setId(2);
        transaction3.setId(5);

        transaction1.sign(privateKey);
        transaction2.sign(privateKey);
        transaction3.sign(privateKey);

        List<Transaction> transactionList = new LinkedList<>(Arrays.asList(
                transaction1,
                transaction2,
                transaction3
        ));

        Block block = new Block(1, 1, "0");

        block.setHash("00A1");

        var transactions = Block.class.getDeclaredField("transactions");

        transactions.setAccessible(true);
        transactions.set(block, transactionList);

        List<Block> blockList = new LinkedList<>(Collections.singletonList(block));

        var blocks = Blockchain.class.getDeclaredField("blockchain");

        blocks.setAccessible(true);
        blocks.set(blockchain, blockList);

        Assertions.assertFalse(blockchain.isValidChain());
    }

    @Test
    void getAllTransactions_GetAllTransactionFromAllBlocksInBlockchain_Succeed() throws NegativeAmountException,
                                                                                        NoSuchAlgorithmException,
                                                                                        SignatureException,
                                                                                        InvalidKeyException,
                                                                                        IOException {
        Blockchain blockchain = new Blockchain(2, reward, 1);

        User miner = new Miner("Damian", blockchain, new VirtualCoin(), transactionPool);
        User client = new Miner("Theresa", blockchain, new VirtualCoin(), transactionPool);
        Resources resources = new Resources(new VirtualCoin(), 5);
        Block block1 = new Block(1, 1, "0");
        Block block2 = new Block(2, 1, "00A1");
        Transaction transaction1 = new Transaction(miner, client, resources, publicKey);
        Transaction transaction2 = new Transaction(client, miner, resources, publicKey);

        miner.getWallet().addAmount(100);
        client.getWallet().addAmount(100);

        block1.setHash("00A1");
        block2.setHash("00B1");

        blockchain.addTransaction(transaction1, privateKey);
        blockchain.addTransaction(transaction2, privateKey);

        blockchain.addBlock(block1, miner);

        blockchain.addTransaction(transaction1, privateKey);
        blockchain.addTransaction(transaction2, privateKey);
        blockchain.addTransaction(transaction1, privateKey);
        blockchain.addTransaction(transaction2, privateKey);

        blockchain.addBlock(block2, miner);

        Assertions.assertEquals(6, blockchain.getAllTransactions().size());
    }
}