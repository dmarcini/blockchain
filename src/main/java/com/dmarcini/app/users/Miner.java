package com.dmarcini.app.users;

import com.dmarcini.app.blockchain.Block;
import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.blockchain.TransactionPool;
import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.reward.Cryptocurrency;
import com.dmarcini.app.utils.cryptography.HashGenerator;

public final class Miner extends User {
    public Miner(String name, Blockchain blockchain, Cryptocurrency cryptocurrency,
                 TransactionPool transactionPool, int startAmount) throws NegativeAmountException {
        super(name, blockchain, cryptocurrency, transactionPool, startAmount);
    }

    @Override
    public void run() {
        while (blockchain.isBlockToMining()) {
            try {
                mineBlock();
            } catch (NegativeAmountException e) {
                e.printStackTrace();
            }
        }
    }

    private void mineBlock() throws NegativeAmountException {
        Block block = blockchain.getNotMinedBlock();

        int nonce = generator.nextInt(Integer.MAX_VALUE);

        String hash = HashGenerator.applySHA256(Long.toString(block.getId()) +
                                                block.getTimestamp() +
                                                block.getPrevHash() +
                                                nonce);

        synchronized (blockchain) {
            block.setHash(hash);

            blockchain.addBlock(block, this);
        }
    }
}
