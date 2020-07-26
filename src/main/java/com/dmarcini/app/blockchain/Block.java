package com.dmarcini.app.blockchain;

import com.dmarcini.app.utils.HashGenerator;

import java.io.Serializable;

public class Block implements Serializable {
    private final long id;
    private long timestamp;
    private int nonce;
    private final String prevBlockHash;
    private String curBlockHash;
    private long creatorId;
    private long timeGeneration;

    public Block(long id, long timestamp, String prevBlockHash) {
        this.id = id;
        this.timestamp = timestamp;
        this.prevBlockHash = prevBlockHash;
    }

     public Block(long id, String prevBlockHash,
                 String curBlockHash, long timeGeneration) {
        this.id = id;
        this.prevBlockHash = prevBlockHash;
        this.curBlockHash = curBlockHash;
        this.timeGeneration = timeGeneration;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public String getCurBlockHash() {
        return curBlockHash;
    }

    public long getTimeGeneration() {
        return timeGeneration;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public void setTimeGeneration(long timeGeneration) {
        this.timeGeneration = timeGeneration;
    }

    public void mine(int nonce) {
        this.nonce = nonce;

        curBlockHash = HashGenerator.applySHA256( String.valueOf(id) +
                                                 timestamp +
                                                 prevBlockHash +
                                                 nonce);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Block:\n");
        stringBuilder.append("Created by miner # ").append(creatorId).append("\n");
        stringBuilder.append("Id: ").append(id).append("\n");
        stringBuilder.append("Timestamp: ").append(timestamp).append("\n");
        stringBuilder.append("Nonce: ").append(nonce).append("\n");
        stringBuilder.append("Hash of the previous block: ").append("\n");
        stringBuilder.append(prevBlockHash).append("\n");
        stringBuilder.append("Hash of the block: ").append("\n");
        stringBuilder.append(curBlockHash).append("\n");
        stringBuilder.append("Block was generating for ").append(timeGeneration).append(" seconds\n\n");

        return stringBuilder.toString();
    }
}
