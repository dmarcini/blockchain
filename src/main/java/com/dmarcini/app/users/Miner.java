package com.dmarcini.app.users;

import com.dmarcini.app.blockchainsystem.Block;
import com.dmarcini.app.blockchainsystem.Blockchain;
import com.dmarcini.app.utils.cryptography.HashGenerator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Miner extends User {
    private static final int BLOCKS_NUMBER_TO_GENERATE = 5;

    private static final Random generator = new Random();
    private static final AtomicLong minerIdGenerator = new AtomicLong(1);

    public Miner(String name, Blockchain blockchain, AtomicBoolean isEnd) {
        super(minerIdGenerator.getAndIncrement(), name, blockchain, isEnd);
    }

    @Override
    public void run() {
        while (blockchain.getSize() < BLOCKS_NUMBER_TO_GENERATE) {
            mineBlock();
        }

        isEnd.set(true);
    }

    private void mineBlock() {
        synchronized (blockchain) {
            Block block = blockchain.getCurBlock();

            int nonce = generator.nextInt(Integer.MAX_VALUE);

            String hash = HashGenerator.applySHA256(Long.toString(block.getId()) +
                    block.getTimestamp() +
                    block.getPrevHash() +
                    nonce);

            blockchain.addBlock(block, id);
        }
    }
}
