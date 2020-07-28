package com.dmarcini.app.blockchain;

import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.resources.Resources;
import com.dmarcini.app.reward.VirtualCoin;
import com.dmarcini.app.users.Client;
import com.dmarcini.app.users.User;
import com.dmarcini.app.utils.cryptography.ObjectSignature;
import com.dmarcini.app.utils.cryptography.RSAKeysGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.*;

class TransactionTest {
    private static PublicKey publicKey;
    private static PrivateKey privateKey;
    private static PrivateKey secondPrivateKey;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException {
        RSAKeysGenerator rsaKeysGenerator = new RSAKeysGenerator(1024);

        rsaKeysGenerator.generateKeys();

        publicKey = rsaKeysGenerator.getPublicKey();
        privateKey = rsaKeysGenerator.getPrivateKey();

        rsaKeysGenerator.generateKeys();

        secondPrivateKey = rsaKeysGenerator.getPrivateKey();
    }

    @Test
    void sign_SignTransactionWithCorrectKey_Succeed() throws NoSuchAlgorithmException, InvalidKeyException,
                                                             IOException, SignatureException {
        Blockchain blockchain = new Blockchain(0, new Resources(new VirtualCoin(), 100), 5);
        TransactionPool transactionPool = new TransactionPool(new VirtualCoin());

        User from = new Client("Mary", blockchain, new VirtualCoin(), transactionPool);
        User to = new Client("Henry", blockchain, new VirtualCoin(), transactionPool);

        Resources resources = new Resources(new VirtualCoin(), 50);

        Transaction transaction = new Transaction(from, to, resources, publicKey);

        transaction.setId(1);
        transaction.sign(privateKey);

        Assertions.assertTrue(ObjectSignature.verify(transaction, transaction.getSignature(), publicKey));
    }

    @Test
    void sign_SignTransactionWithIncorrectKey_Failed() throws NoSuchAlgorithmException, InvalidKeyException,
            IOException, SignatureException {
        Blockchain blockchain = new Blockchain(0, new Resources(new VirtualCoin(), 100), 5);
        TransactionPool transactionPool = new TransactionPool(new VirtualCoin());

        User from = new Client("Mary", blockchain, new VirtualCoin(), transactionPool);
        User to = new Client("Henry", blockchain, new VirtualCoin(), transactionPool);

        Resources resources = new Resources(new VirtualCoin(), 50);

        Transaction transaction = new Transaction(from, to, resources, publicKey);

        transaction.setId(1);
        transaction.sign(secondPrivateKey);

        Assertions.assertFalse(ObjectSignature.verify(transaction, transaction.getSignature(), publicKey));
    }

    @Test
    void execute_ExecuteTransactionWithEnoughResources_ResourcesChanged() throws NoSuchAlgorithmException,
                                                                                 NegativeAmountException {
        Blockchain blockchain = new Blockchain(0, new Resources(new VirtualCoin(), 100), 5);
        TransactionPool transactionPool = new TransactionPool(new VirtualCoin());

        User from = new Client("Mary", blockchain, new VirtualCoin(), transactionPool);
        User to = new Client("Henry", blockchain, new VirtualCoin(), transactionPool);

        Resources resources = new Resources(new VirtualCoin(), 50);

        from.getWallet().addAmount(50);

        Transaction transaction = new Transaction(from, to, resources, publicKey);

        transaction.execute();

        Assertions.assertEquals(0, from.getWallet().getResources().getAmount());
        Assertions.assertEquals(50, to.getWallet().getResources().getAmount());
    }

    @Test
    void execute_ExecuteTransactionWithoutEnoughResources_ResourcesNotChanged() throws NoSuchAlgorithmException,
                                                                                       NegativeAmountException {
        Blockchain blockchain = new Blockchain(0, new Resources(new VirtualCoin(), 100), 5);
        TransactionPool transactionPool = new TransactionPool(new VirtualCoin());

        User from = new Client("Mary", blockchain, new VirtualCoin(), transactionPool);
        User to = new Client("Henry", blockchain, new VirtualCoin(), transactionPool);

        Resources resources = new Resources(new VirtualCoin(), 100);

        from.getWallet().addAmount(50);

        Transaction transaction = new Transaction(from, to, resources, publicKey);

        transaction.execute();

        Assertions.assertEquals(50, from.getWallet().getResources().getAmount());
        Assertions.assertEquals(0, to.getWallet().getResources().getAmount());
    }
}
