package com.dmarcini.app.blockchain;

import com.dmarcini.app.stringutil.StringUtil;

import java.util.Date;

public class Block {
    private long id;
    private long timestamp;
    private String previousBlockHash;
    private String hash;

    public Block(String previousBlockHash) {
        this.id = IDGenerator.getId();
        this.timestamp = new Date().getTime();
        this.previousBlockHash = previousBlockHash;
        this.hash = StringUtil.applySHA256(String.valueOf(id) +
                                           timestamp +
                                           this.previousBlockHash);
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Block:\n");
        stringBuilder.append("Id: ").append(id).append("\n");
        stringBuilder.append("Timestamp: ").append(timestamp).append("\n");
        stringBuilder.append("Hash of the previous block: ").append("\n");
        stringBuilder.append(previousBlockHash).append("\n");
        stringBuilder.append("Hash of the block: ").append("\n");
        stringBuilder.append(hash).append("\n");

        return stringBuilder.toString();
    }

    static class IDGenerator {
        private static long id = 0;

        static long getId() {
            return id++;
        }
    }
}
