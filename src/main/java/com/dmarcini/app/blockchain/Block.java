package com.dmarcini.app.blockchain;

import com.dmarcini.app.stringutil.StringUtil;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

public class Block implements Serializable {
    private long id;
    private long timestamp;
    private int magicNumber;
    private String prevBlockHash;
    private String curBlockHash;
    private long timeHashGeneration;

    public Block(int startZerosNum, String prevBlockHash) {
        this.id = IDGenerator.getId();
        this.timestamp = new Date().getTime();
        this.prevBlockHash = prevBlockHash;
        this.curBlockHash = generateHash(startZerosNum);
    }

    public long getId() {
        return id;
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
        stringBuilder.append("Id: ").append(id).append("\n");
        stringBuilder.append("Timestamp: ").append(timestamp).append("\n");
        stringBuilder.append("Magic number: ").append(magicNumber).append("\n");
        stringBuilder.append("Hash of the previous block: ").append("\n");
        stringBuilder.append(prevBlockHash).append("\n");
        stringBuilder.append("Hash of the block: ").append("\n");
        stringBuilder.append(curBlockHash).append("\n");
        stringBuilder.append("Block was generating for ").append(timeHashGeneration).append(" seconds\n");

        return stringBuilder.toString();
    }

    private String generateHash(int startZerosNum) {
        Random generator = new Random();
        Instant start = Instant.now();

        String hash;

        do {
            magicNumber = generator.nextInt(Integer.MAX_VALUE);

            hash = StringUtil.applySHA256(id +
                                          String.valueOf(timestamp) +
                                          magicNumber +
                                          this.prevBlockHash);

        } while(!hash.startsWith("0".repeat(startZerosNum)));

        timeHashGeneration = Duration.between(start, Instant.now()).toSeconds();

        return hash;
    }

    private static class IDGenerator  {
        private static long id = 1;

        public static long getId() {
            return id++;
        }
    }
}
