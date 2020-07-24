package com.dmarcini.app.blockchain;

import java.io.Serializable;

public class Block implements Serializable {
    private long id;
    private long timestamp;
    private int magicNumber;
    private String prevBlockHash;
    private String curBlockHash;
    private long createdBy;
    private long timeGeneration;

    public Block(long id, long timestamp, int magicNumber,
                 String prevBlockHash, String curBlockHash,
                 long createdBy, long timeGeneration) {
        this.id = id;
        this.timestamp = timestamp;
        this.magicNumber = magicNumber;
        this.prevBlockHash = prevBlockHash;
        this.curBlockHash = curBlockHash;
        this.createdBy = createdBy;
        this.timeGeneration = timeGeneration;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public String getCurBlockHash() {
        return curBlockHash;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Block:\n");
        stringBuilder.append("Created by miner # ").append(createdBy).append("\n");
        stringBuilder.append("Id: ").append(id).append("\n");
        stringBuilder.append("Timestamp: ").append(timestamp).append("\n");
        stringBuilder.append("Magic number: ").append(magicNumber).append("\n");
        stringBuilder.append("Hash of the previous block: ").append("\n");
        stringBuilder.append(prevBlockHash).append("\n");
        stringBuilder.append("Hash of the block: ").append("\n");
        stringBuilder.append(curBlockHash).append("\n");
        stringBuilder.append("Block was generating for ").append(timeGeneration).append(" seconds\n");

        return stringBuilder.toString();
    }
}
