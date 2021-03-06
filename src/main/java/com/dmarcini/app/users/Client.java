package com.dmarcini.app.users;

import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.blockchain.Transaction;
import com.dmarcini.app.blockchain.TransactionPool;
import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.resources.Resources;
import com.dmarcini.app.reward.Cryptocurrency;
import com.dmarcini.app.utils.cryptography.RSAKeysGenerator;

import java.io.IOException;
import java.security.*;
import java.util.Optional;

public final class Client extends User {
    private static final int KEY_LENGTH = 1024;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public Client(String name, Blockchain blockchain, Cryptocurrency cryptocurrency,
                  TransactionPool transactionPool, int startAmount) throws NoSuchAlgorithmException,
                                                                           NegativeAmountException {
        super(name, blockchain, cryptocurrency, transactionPool, startAmount);

        RSAKeysGenerator RSAKeysGenerator = new RSAKeysGenerator(KEY_LENGTH);

        RSAKeysGenerator.generateKeys();

        this.privateKey = RSAKeysGenerator.getPrivateKey();
        this.publicKey = RSAKeysGenerator.getPublicKey();
    }

    @Override
    public void run() {
        try {
            wallet.addAmount(100);
        } catch (NegativeAmountException e) {
            e.printStackTrace();
        }

        while (blockchain.isBlockToMining()) {
            try {
                doTransaction();
            } catch (NoSuchAlgorithmException | SignatureException |
                     InvalidKeyException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doTransaction() throws NoSuchAlgorithmException, SignatureException,
                                        InvalidKeyException, IOException, InterruptedException {
        Optional<User> to;

        if (transactionPool.getUsersNum() > 0) {
            do {
                to = transactionPool.getUser(generator.nextInt(transactionPool.getUsersNum()));
            } while(to.get().getId() == id);

            Resources resources = new Resources(transactionPool.getCryptocurrency(), generator.nextInt(100));
            Transaction transaction = new Transaction(this, to.get(), resources, publicKey);

            synchronized (blockchain) {
                blockchain.addTransaction(transaction, privateKey);
            }
        }

        Thread.sleep(500 + generator.nextInt(500));
    }
}
