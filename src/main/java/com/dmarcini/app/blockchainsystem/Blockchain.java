package com.dmarcini.app.blockchain;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class Blockchain implements Serializable {
    private final static AtomicLong blockIdGenerator = new AtomicLong(1);
    private final static AtomicLong messageIdGenerator = new AtomicLong(1);

    private final ArrayList<Block> blocks;
    private final ArrayList<Message> messages;
    private int startZerosNum;
    private Block curBlock;

    private Instant start;

    public Blockchain(int startZerosNum) {
        this.blocks = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.startZerosNum = startZerosNum;
        this.start = Instant.now();

        generateNextBlock();
    }

    public int getSize() {
        return blocks.size();
    }

    public Optional<Block> getBlock(int blockNum) {
        boolean isBlockExists = (blockNum < 0 || blockNum >= blocks.size());

        return isBlockExists ? Optional.empty() : Optional.of(blocks.get(blockNum));
    }

    public boolean addBlock(Block block, long creatorId) {
        if (!isValidBlock(block)) {
            return false;
        }

        block.setTimeGeneration(Duration.between(start, Instant.now()).toSeconds());
        block.setCreatorId(creatorId);
        block.setMessages(messages);

        blocks.add(block);

        generateNextBlock();
        regulateStartZerosNum();
        resetTimer();
        clearMessages();

        return true;
    }

    public void addMessage(Message message,
                           PrivateKey privateKey) throws SignatureException, NoSuchAlgorithmException,
                                                            InvalidKeyException, IOException {
        message.setId(messageIdGenerator.getAndIncrement());
        message.sign(privateKey);

        messages.add(message);
    }

    public Block getCurBlock() {
        return curBlock;
    }

    public Boolean isValidChain() {
        return areValidPrevHash() && areValidMessagesId();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        blocks.forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    private boolean isValidBlock(Block block) {
        String prevBlockHash = getLastBlock().isPresent() ? getLastBlock().get().getCurBlockHash() : "0";

        return block.getCurBlockHash().startsWith("0".repeat(startZerosNum)) &&
               block.getPrevBlockHash().equals(prevBlockHash);
    }

    private boolean areValidPrevHash() {
        for (int i = 1; i < blocks.size(); ++i) {
            if (!blocks.get(i).getPrevBlockHash().equals(blocks.get(i - 1).getCurBlockHash())) {
                return false;
            }
        }

        return true;
    }

    private boolean areValidMessagesId() {
        long messageId = -1;

        for (var block : blocks) {
            for (var message : block.getMessages()) {
                if (message.getId() <= messageId) {
                    return false;
                }

                messageId = message.getId();
            }
        }

        return true;
    }

    private void regulateStartZerosNum() {
        long timeGeneration = getLastBlock().isPresent() ? getLastBlock().get().getTimeGeneration() : 0;

        if (timeGeneration <= 1) {
            ++startZerosNum;
        } else if(timeGeneration > 60) {
            --startZerosNum;
        }
    }

    private void generateNextBlock() {
        long blockId = blockIdGenerator.getAndIncrement();
        long timestamp = new Date().getTime();
        String prevBlockHash = getLastBlock().isPresent() ? getLastBlock().get().getCurBlockHash() : "0";

        curBlock = new Block(blockId, timestamp, prevBlockHash);
    }

    private Optional<Block> getLastBlock() {
        return blocks.isEmpty() ? Optional.empty() : Optional.of(blocks.get(blocks.size() - 1));
    }

    private void resetTimer() {
        start = Instant.now();
    }

    private void clearMessages() {
        messages.clear();
    }
}
