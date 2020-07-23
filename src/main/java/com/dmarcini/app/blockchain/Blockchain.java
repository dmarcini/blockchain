package com.dmarcini.app.blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Blockchain implements Serializable {
    private ArrayList<Block> blockchain;
    private int startZerosNum;

    Blockchain(int startZerosNum) {
        this.blockchain = new ArrayList<>();
        this.startZerosNum = startZerosNum;
    }

    public void generateBlocks(int blocksNum) {
        String prevBlockHash;

        if (blockchain.isEmpty()) {
            prevBlockHash = "0";
        } else {
            prevBlockHash = blockchain.get(blockchain.size() - 1).getCurBlockHash();
        }

        for (int i = 0; i < blocksNum; ++i) {
            Block block = new Block(startZerosNum, prevBlockHash);
            prevBlockHash = block.getCurBlockHash();

            blockchain.add(block);
        }
    }

    public void removeAllBlocks() {
        blockchain.clear();
    }

    public int getSize() {
        return blockchain.size();
    }

    public Optional<Block> getBlock(int blockNum) {
        if (blockNum < 0 || blockNum >= blockchain.size()) {
            return Optional.empty();
        }

        return Optional.of(blockchain.get(blockNum));
    }

    public void addBlock() {
        String lastBlockHash = "0";

        if (!blockchain.isEmpty()) {
            lastBlockHash = blockchain.get(blockchain.size() - 1).getCurBlockHash();
        }

        blockchain.add(new Block(startZerosNum, lastBlockHash));
    }

    public boolean removeBlock(int blockNum) {
        if (blockNum < 0 || blockNum >= blockchain.size()) {
            return false;
        }

        blockchain.remove(blockNum);

        return true;
    }

    public Boolean isValid() {
        if (blockchain.isEmpty()) {
            return true;
        }

        String prevBlockHash = "0";

        for (var block : blockchain) {
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

        for (var block : blockchain) {
            stringBuilder.append(block.toString()).append("\n");
        }

        return stringBuilder.toString();
    }
}
