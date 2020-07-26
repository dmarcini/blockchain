package com.dmarcini.app.users;

import com.dmarcini.app.blockchain.Block;
import com.dmarcini.app.blockchain.Blockchain;

import java.util.Random;

public class Miner implements Runnable {
    private static final int BLOCKS_NUMBER_TO_GENERATE = 7;

    private static final Random generator = new Random();

    private final long id;
    private final Blockchain blockchain;

    public Miner(Blockchain blockchain) {
        this.id = MinerIDGenerator.getId();
        this.blockchain = blockchain;
    }

    public void mineBlock() {
        synchronized (blockchain) {
            Block curBlock = blockchain.getCurBlock();

            curBlock.mine(generator.nextInt(Integer.MAX_VALUE));

            blockchain.addBlock(curBlock, id);
        }
    }

    @Override
    public void run() {
        while (blockchain.getSize() < BLOCKS_NUMBER_TO_GENERATE) {
            mineBlock();
        }
    }

    private static class MinerIDGenerator {
        private static long id = 1;

        public static long getId() {
            return id++;
        }
    }
}
