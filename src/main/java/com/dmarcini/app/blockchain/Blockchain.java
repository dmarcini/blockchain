package com.dmarcini.app.blockchainsystem;

import com.dmarcini.app.reward.Cryptocurrency;
import com.dmarcini.app.users.User;
import com.dmarcini.app.utils.Timer;
import com.dmarcini.app.utils.cryptography.ObjectSignature;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class Blockchain implements Serializable {
    private final static int MAX_NUM_BLOCKS_TO_MINING = 5;

    private final AtomicLong blockIdGenerator = new AtomicLong(1);
    private final AtomicLong transactionIdGenerator = new AtomicLong(1);

    private final AtomicBoolean areAllBlocksMined = new AtomicBoolean(false);

    private final List<Block> blockchain;
    private final List<Transaction> transactions;
    private int difficultLevel;
    private final Map<Cryptocurrency, Long> rewardForMinedBlock;
    private Block notMinedBlock;
    private final Timer timer;

    public Blockchain(int difficultLevel, Map<Cryptocurrency, Long> rewardForMinedBlock) {
        this.blockchain = new LinkedList<>();
        this.transactions = new LinkedList<>();
        this.difficultLevel = difficultLevel;
        this.rewardForMinedBlock = rewardForMinedBlock;
        this.timer = new Timer();

        generateNewBlock();
    }

    public int getSize() {
        return blockchain.size();
    }

    public Block getNotMinedBlock() {
        return notMinedBlock;
    }

    public Optional<Block> getBlock(int blockNum) {
        boolean isBlockExists = (blockNum < 0 || blockNum >= blockchain.size());

        return isBlockExists ? Optional.empty() : Optional.of(new Block(blockchain.get(blockNum)));
    }

    public synchronized boolean addBlock(Block block, User miner) {
        if (!isValidBlock(block)) {
            return false;
        }

        block.setTimeGeneration(timer.elapsed());
        block.setCreator(miner);
        block.setTransactions(transactions);

        miner.addResources(rewardForMinedBlock);

        blockchain.add(new Block(block));

        generateNewBlock();
        regulateDifficultLevel();
        resetTimer();
        clearTransactions();
        checkIfAllBlocksMined();

        return true;
    }

    public synchronized void addTransaction(Transaction transaction,
                                            PrivateKey privateKey) throws SignatureException,
                                                                          NoSuchAlgorithmException,
                                                                          InvalidKeyException, IOException {
        if (isValidTransaction(transaction)) {
            transaction.setId(transactionIdGenerator.getAndIncrement());
            transaction.sign(privateKey);

            transactions.add(new Transaction(transaction));
        }
    }

    public Boolean isValidChain() throws SignatureException, NoSuchAlgorithmException,
                                         InvalidKeyException, IOException {
        return areValidPrevHashes() && areValidTransactions();
    }

    public boolean areAllBlocksMined() {
        return areAllBlocksMined.get();
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        blockchain.forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    private boolean isValidBlock(Block block) {
        String prevBlockHash = getLastBlock().isPresent() ? getLastBlock().get().getHash() : "0";

        return block.getHash().startsWith("0".repeat(difficultLevel)) &&
               block.getPrevHash().equals(prevBlockHash);
    }

    private boolean areValidPrevHashes() {
        for (int i = 1; i < blockchain.size(); ++i) {
            if (!blockchain.get(i).getPrevHash().equals(blockchain.get(i - 1).getHash())) {
                return false;
            }
        }

        return true;
    }

    private boolean areValidTransactions() throws IOException, NoSuchAlgorithmException,
                                                  InvalidKeyException, SignatureException {
        long transactionId = -1;

        for (var block : blockchain) {
            for (var transaction : block.getTransactions()) {
                if (transaction.getId() <= transactionId ||
                    !ObjectSignature.verify(transaction, transaction.getSignature(),
                                            transaction.getPublicKey())) {
                    return false;
                }

                transactionId = transaction.getId();
            }
        }

        return true;
    }

    private boolean isValidTransaction(Transaction transaction) {
        var resourcesFrom = transaction.getFrom().getWallet().getResources();

        return transaction.getResources()
                          .entrySet()
                          .stream()
                          .allMatch(r -> r.getValue() <= resourcesFrom.get(r.getKey()));
    }

    private void regulateDifficultLevel() {
        long timeGeneration = getLastBlock().isPresent() ? getLastBlock().get().getTimeGeneration() : 0;

        if (timeGeneration <= 1) {
            ++difficultLevel;
        } else if(timeGeneration > 60) {
            --difficultLevel;
        }
    }

    private void generateNewBlock() {
        long blockId = blockIdGenerator.getAndIncrement();
        long timestamp = new Date().getTime();
        String prevBlockHash = getLastBlock().isPresent() ? getLastBlock().get().getHash()
                                                          : "0";

        notMinedBlock = new Block(blockId, timestamp, prevBlockHash);
    }

    private Optional<Block> getLastBlock() {
        return blockchain.isEmpty() ? Optional.empty()
                                    : Optional.of(new Block(blockchain.get(blockchain.size() - 1)));
    }

    private void checkIfAllBlocksMined() {
        if (blockchain.size() >= MAX_NUM_BLOCKS_TO_MINING) {
            areAllBlocksMined.set(true);
        }
    }

    private void resetTimer() {
        timer.reset();
    }

    private void clearTransactions() {
        transactions.clear();
    }
}
