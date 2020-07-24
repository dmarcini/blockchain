package com.dmarcini.app.miner;

import com.dmarcini.app.blockchain.Block;
import com.dmarcini.app.blockchain.Blockchain;
import com.dmarcini.app.stringutil.StringUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

public class Miner implements Runnable {
    private final static int BLOCKS_NUMBER_TO_GENERATE = 5;

    private static final Random generator = new Random();

    private long id;
    private final Blockchain blockchain;

    private Instant start;

    public Miner(Blockchain blockchain) {
        this.id = IDGenerator.getId();
        this.blockchain = blockchain;
        this.start = Instant.now();
    }

    public void mine() {
        long blockID = blockchain.getNextBlockId();
        String prevBlockHash = blockchain.getLastBlockHash();

        long timestamp = new Date().getTime();
        int magicNumber = generator.nextInt(Integer.MAX_VALUE);

        String curBlockHash = StringUtil.applySHA256(blockID +
                                                     String.valueOf(timestamp) +
                                                     prevBlockHash + magicNumber);
        long timeGeneration = Duration.between(start, Instant.now()).toSeconds();

        Block block = new Block(blockID, timestamp, magicNumber, prevBlockHash,
                                curBlockHash, id, timeGeneration);

        synchronized (blockchain) {
            if (blockchain.addBlock(block)) {
                start = Instant.now();
            }
        }
    }

    @Override
    public void run() {
        while (blockchain.getSize() < BLOCKS_NUMBER_TO_GENERATE) {
            mine();
        }
    }

    private static class IDGenerator {
        private static long id = 1;

        public static long getId() {
            return id++;
        }
    }
}
