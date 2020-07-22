package com.dmarcini.app.blockchain;

import java.util.ArrayList;

public class Blockchain {
    private ArrayList<Block> blockchain;

    Blockchain() {
        blockchain = new ArrayList<>();
    }

    public void generateBlocks(int blocksNumber) {
        String hashPreviousBlock = "0";

        for (int i = 0; i < blocksNumber; ++i) {
            Block block = new Block(hashPreviousBlock);
            hashPreviousBlock = block.getHash();

            blockchain.add(block);
        }
    }

    public void removeAllBlocks() {
        blockchain.clear();
    }

    public int getSize() {
        return blockchain.size();
    }

    public Block getBlock(int blockNumber) {
        return blockchain.get(blockNumber);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (var block : blockchain) {
            stringBuilder.append(block.toString()).append("\n");
        }

        return stringBuilder.toString();
    }
}
