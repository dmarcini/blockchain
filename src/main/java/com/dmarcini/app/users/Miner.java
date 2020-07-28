package com.dmarcini.app.users;

import com.dmarcini.app.blockchain.Block;
import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.blockchain.TransactionPool;
import com.dmarcini.app.reward.Cryptocurrency;
import com.dmarcini.app.utils.cryptography.HashGenerator;

public final class Miner extends User {
    public Miner(String name, Blockchain blockchain,
                 Cryptocurrency cryptocurrency, TransactionPool transactionPool) {
        super(name, blockchain, cryptocurrency, transactionPool);
    }

    @Override
    public void run() {
        while (!blockchain.areAllBlocksMined()) {
            mineBlock();
        }
    }

    private void mineBlock() {
        synchronized (blockchain) {
            Block block = blockchain.getNotMinedBlock();

            int nonce = generator.nextInt(Integer.MAX_VALUE);

            String hash = HashGenerator.applySHA256(Long.toString(block.getId()) +
                                                    block.getTimestamp() +
                                                    block.getPrevHash() +
                                                    nonce);

            block.setHash(hash);

            blockchain.addBlock(block, this);
        }
    }
}
