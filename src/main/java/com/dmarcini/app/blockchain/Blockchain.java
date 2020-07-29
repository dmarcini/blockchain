package com.dmarcini.app.blockchain;

import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.resources.Resources;
import com.dmarcini.app.users.User;
import com.dmarcini.app.utils.Timer;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class Blockchain implements Serializable {
    private final AtomicLong blockIdGenerator = new AtomicLong(1);
    private final AtomicLong transactionIdGenerator = new AtomicLong(1);

    private final AtomicBoolean areAllBlocksMined = new AtomicBoolean(false);

    private final List<Block> blockchain;
    private final List<Transaction> transactions;
    private int difficultLevel;
    private final Resources reward;
    private final int maxNumBlockToMining;
    private Block notMinedBlock;
    private final Timer timer;

    public Blockchain(int difficultLevel, Resources reward, int maxNumBlockToMining) {
        this.blockchain = new LinkedList<>();
        this.transactions = new LinkedList<>();
        this.difficultLevel = difficultLevel;
        this.reward = reward;
        this.maxNumBlockToMining = maxNumBlockToMining;
        this.timer = new Timer();

        generateNewBlock();
    }

    public int getSize() {
        return blockchain.size();
    }

    public Block getNotMinedBlock() {
        return notMinedBlock;
    }

    public void addBlock(Block block, User miner) throws NegativeAmountException {
        if (isValidBlock(block)) {
            block.setMiner(miner);
            block.setTransactions(transactions);

            miner.getWallet().addAmount(reward.getAmount());

            blockchain.add(new Block(block));

            executeTransactions();
            updateStatus();
        }
    }

    public void addTransaction(Transaction transaction,
                               PrivateKey privateKey) throws SignatureException, NoSuchAlgorithmException,
                                                             InvalidKeyException, IOException {
        transaction.setId(transactionIdGenerator.getAndIncrement());
        transaction.sign(privateKey);

        transactions.add(new Transaction(transaction));
    }

    public List<Transaction> getAllTransactions() {
        return blockchain.stream()
                         .map(Block::getTransactions)
                         .flatMap(Collection::stream)
                         .collect(Collectors.toList());
    }

    public Boolean isValidChain() {
        return areValidPrevHashes() && areValidTransactions();
    }

    public boolean isBlockToMining() {
        return !areAllBlocksMined.get();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        blockchain.forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    private boolean isValidBlock(Block block) {
        String prevBlockHash = getLastBlock().isPresent() ? getLastBlock().get().getHash() : "0";

        boolean startsWithCorrectZerosNum = block.getHash().startsWith("0".repeat(difficultLevel));
        boolean arePrevHashEquals = block.getPrevHash().equals(prevBlockHash);

        return startsWithCorrectZerosNum && arePrevHashEquals;
    }

    private boolean areValidPrevHashes() {
        for (int i = 1; i < blockchain.size(); ++i) {
            if (!blockchain.get(i).getPrevHash().equals(blockchain.get(i - 1).getHash())) {
                return false;
            }
        }

        return true;
    }

    private boolean areValidTransactions() {
        long transactionId = -1;

        for (var block : blockchain) {
            for (var transaction : block.getTransactions()) {
                if (transaction.getId() <= transactionId) {
                    return false;
                }

                transactionId = transaction.getId();
            }
        }

        return true;
    }

    private Optional<Block> getLastBlock() {
        return blockchain.isEmpty() ? Optional.empty()
                                    : Optional.of(new Block(blockchain.get(blockchain.size() - 1)));
    }

    private void executeTransactions() {
        for (var transaction : transactions) {
            transaction.execute();
        }

        transactions.clear();
    }

    private void updateStatus() {
        if (blockchain.size() >= maxNumBlockToMining) {
            areAllBlocksMined.set(true);
        } else {
            regulateDifficultLevel();
            generateNewBlock();
        }
    }

    private void regulateDifficultLevel() {
        long timeGeneration = timer.elapsed();

        if (timeGeneration <= 1) {
            ++difficultLevel;
        } else if(timeGeneration > 60) {
            --difficultLevel;
        }

        timer.reset();
    }

    private void generateNewBlock() {
        long blockId = blockIdGenerator.getAndIncrement();
        long timestamp = new Date().getTime();
        String prevBlockHash = getLastBlock().isPresent() ? getLastBlock().get().getHash() : "0";

        notMinedBlock = new Block(blockId, timestamp, prevBlockHash);
    }
}
