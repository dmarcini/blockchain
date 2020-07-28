package com.dmarcini.app;

import com.dmarcini.app.reward.VirtualCoin;
import com.dmarcini.app.system.BlockchainSystem;

import java.security.NoSuchAlgorithmException;

public class Main {
    private final static int MINERS_NUM = 5;
    private final static int CLIENTS_NUM = 5;
    private final static int START_DIFFICULT_LEVEL = 0;
    private final static int MAX_NUM_BLOCK_TO_MINING = 5;
    private final static int REWARD_AMOUNT = 100;

    private static BlockchainSystem blockchainSystem = null;

    static {
        try {
            blockchainSystem = new BlockchainSystem(MINERS_NUM, CLIENTS_NUM, START_DIFFICULT_LEVEL,
                                                    MAX_NUM_BLOCK_TO_MINING, REWARD_AMOUNT,
                                                    new VirtualCoin());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        if (blockchainSystem != null) {
            blockchainSystem.start();

            System.out.println(blockchainSystem.getBlockchain());
        }
    }
}