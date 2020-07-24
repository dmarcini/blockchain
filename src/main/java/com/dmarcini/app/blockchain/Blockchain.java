package com.dmarcini.app.blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Blockchain implements Serializable {
    private ArrayList<Block> blocks;
    private int startZerosNum;
    private String lastBlockHash;
    private long lastBlockTimeGeneration;
    private long nextBlockId;

    public Blockchain(int startZerosNum) {
        this.blocks = new ArrayList<>();
        this.startZerosNum = startZerosNum;
        this.lastBlockHash = "0";
        this.nextBlockId = 1;
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
        boolean isBlockExist = (blockNum < 0 || blockNum >= blocks.size());

        return isBlockExist ? Optional.empty() : Optional.of(blocks.get(blockNum));
    }

    public boolean addBlock(Block block) {
        if (!isValidBlock(block)) {
            return false;
        }

        blocks.add(block);

        nextBlockId += 1;
        lastBlockHash = block.getCurBlockHash();
        lastBlockTimeGeneration = block.getTimeGeneration();

        regulateStartZerosNum();

        return true;
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
        return block.getCurBlockHash().startsWith("0".repeat(startZerosNum)) &&
               block.getPrevBlockHash().equals(lastBlockHash);
    }

    private void regulateStartZerosNum() {
        if (lastBlockTimeGeneration <= 1) {
            ++startZerosNum;
        } else if(lastBlockTimeGeneration > 60) {
            --startZerosNum;
        }
    }
}
