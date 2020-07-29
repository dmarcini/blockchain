package com.dmarcini.app.blockchain;

import com.dmarcini.app.users.User;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public final class Block implements Serializable {
    private final long id;
    private final long timestamp;
    private int nonce;
    private final String prevHash;
    private String hash;
    private List<Transaction> transactions;
    private User miner;

    public Block(long id, long timestamp, String prevHash) {
        this.id = id;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
    }

    public Block(Block block) {
        this.id = block.id;
        this.timestamp = block.timestamp;
        this.nonce = block.nonce;
        this.prevHash = block.prevHash;
        this.hash = block.hash;
        this.transactions = block.transactions.stream()
                                              .map(Transaction::new)
                                              .collect(Collectors.toList());
        this.miner = block.miner;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Transaction> getTransactions() {
        return transactions.stream()
                           .map(Transaction::new)
                           .collect(Collectors.toList());
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions.stream()
                                        .map(Transaction::new)
                                        .collect(Collectors.toList());
    }

    public void setMiner(User miner) {
        this.miner = miner;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Block:\n");
        stringBuilder.append("Created by: ").append(miner.getId()).append("\n");
        stringBuilder.append("Id: ").append(id).append("\n");
        stringBuilder.append("Timestamp: ").append(timestamp).append("\n");
        stringBuilder.append("Nonce: ").append(nonce).append("\n");
        stringBuilder.append("Hash of the previous block: ").append("\n");
        stringBuilder.append(prevHash).append("\n");
        stringBuilder.append("Hash of the block: ").append("\n");
        stringBuilder.append(hash).append("\n");
        stringBuilder.append("Block data:\n");

        for (var transaction : transactions) {
            stringBuilder.append(transaction.getFrom().getName()).append(" sent ")
                         .append(transaction.getResources().getAmount())
                         .append(transaction.getResources().getCryptocurrency().getCurrency())
                         .append(" to ").append(transaction.getTo().getName()).append("\n");
        }

        if (transactions.isEmpty()) {
            stringBuilder.append("no transactions!\n");
        }

        return stringBuilder.append("\n").toString();
    }
}
