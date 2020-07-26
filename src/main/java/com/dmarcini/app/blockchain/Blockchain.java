package com.dmarcini.app.blockchain;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class Blockchain implements Serializable {
    private final ArrayList<Block> blocks;
    private int startZerosNum;
    private Block curBlock;

    private Instant start;

    public Blockchain(int startZerosNum) {
        this.blocks = new ArrayList<>();
        this.startZerosNum = startZerosNum;
        this.start = Instant.now();

        generateNextBlock();
    }

    public int getSize() {
        return blocks.size();
    }

    public Optional<Block> getBlock(int blockNum) {
        boolean isBlockExists = (blockNum < 0 || blockNum >= blocks.size());

        return isBlockExists ? Optional.empty() : Optional.of(blocks.get(blockNum));
    }

    public boolean addBlock(Block block, long creatorId) {
        if (!isValidBlock(block)) {
            return false;
        }

        block.setTimeGeneration(Duration.between(start, Instant.now()).toSeconds());
        block.setCreatorId(creatorId);

        blocks.add(block);

        generateNextBlock();
        regulateStartZerosNum();
        resetTimer();

        return true;
    }

    public Block getCurBlock() {
        return curBlock;
    }

    public Boolean isValidChain() {
        for (int i = 1; i < blocks.size(); ++i) {
            if (!blocks.get(i).getPrevBlockHash().equals(blocks.get(i - 1).getCurBlockHash())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        blocks.forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    private boolean isValidBlock(Block block) {
        String prevBlockHash = getLastBlock().isPresent() ? getLastBlock().get().getCurBlockHash() : "0";

        return block.getCurBlockHash().startsWith("0".repeat(startZerosNum)) &&
               block.getPrevBlockHash().equals(prevBlockHash);
    }

    private void regulateStartZerosNum() {
        long timeGeneration = getLastBlock().isPresent() ? getLastBlock().get().getTimeGeneration() : 0;

        if (timeGeneration <= 1) {
            ++startZerosNum;
        } else if(timeGeneration > 60) {
            --startZerosNum;
        }
    }

    private void generateNextBlock() {
        long blockId = BlockIDGenerator.getId();
        long timestamp = new Date().getTime();
        String prevBlockHash = getLastBlock().isPresent() ? getLastBlock().get().getCurBlockHash() : "0";

        curBlock = new Block(blockId, timestamp, prevBlockHash);
    }

    private Optional<Block> getLastBlock() {
        if (blocks.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(blocks.get(blocks.size() - 1));
    }

    private void resetTimer() {
        start = Instant.now();
    }

    private static class BlockIDGenerator implements Serializable {
        private static long id = 1;

        public static long getId() {
            return id++;
        }
    }
}
