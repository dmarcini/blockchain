package com.dmarcini.app.blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Blockchain implements Serializable {
    private ArrayList<Block> blocks;
    private int startZerosNum;
    private String lastBlockHash;
    private long nextBlockId;
    private long lastBlockTimeGeneration;

    public Blockchain(int startZerosNum) {
        this.blocks = new ArrayList<>();
        this.lastBlockHash = "0";
        this.nextBlockId = 1;
    }

    public int getStartZerosNum() {
        return startZerosNum;
    }

    public String getLastBlockHash() {
        return lastBlockHash;
    }

    public long getNextBlockId() {
        return nextBlockId;
    };

    public void setLastBlockTimeGeneration(long lastBlockTimeGeneration) {
        this.lastBlockTimeGeneration = lastBlockTimeGeneration;
    }

    public int getSize() {
        return blocks.size();
    }

    public Optional<Block> getBlock(int blockNum) {
        if (blockNum < 0 || blockNum >= blocks.size()) {
            return Optional.empty();
        }

        return Optional.of(blocks.get(blockNum));
    }

    public boolean addBlock(Block block) {
        if (!isValidBlock(block)) {
            return false;
        }

        blocks.add(block);

        nextBlockId += 1;
        lastBlockHash = block.getCurBlockHash();

        regulateStartZerosNum();

        return true;
    }

    public Boolean isValidChain() {
        if (blocks.isEmpty()) {
            return true;
        }

        String prevBlockHash = "0";

        for (var block : blocks) {
            if (!block.getPrevBlockHash().equals(prevBlockHash)) {
                return false;
            }

            prevBlockHash = block.getCurBlockHash();
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (var block : blocks) {
            stringBuilder.append(block.toString()).append("\n");
        }

        return stringBuilder.toString();
    }

    private boolean isValidBlock(Block block) {
        if (!block.getCurBlockHash().startsWith("0".repeat(startZerosNum))) {
            return false;
        }

        return lastBlockHash.equals(block.getPrevBlockHash());
    }

    private void regulateStartZerosNum() {
        if (lastBlockTimeGeneration <= 1) {
            ++startZerosNum;
        } else if(lastBlockTimeGeneration > 60) {
            --startZerosNum;
        }
    }
}
