package com.dmarcini.app;

import com.dmarcini.app.resources.NegativeAmountException;
import com.dmarcini.app.reward.VirtualCoin;
import com.dmarcini.app.service.BlockchainService;

import java.security.NoSuchAlgorithmException;

public class Main {
    private final static int MINERS_NUM = 5;
    private final static int CLIENTS_NUM = 5;
    private final static int START_DIFFICULT_LEVEL = 0;
    private final static int MAX_NUM_BLOCK_TO_MINING = 5;
    private final static int REWARD_AMOUNT = 100;
    private final static int START_MINERS_AMOUNT = 0;
    private final static int START_CLIENTS_AMOUNT = 100;

    private static BlockchainService blockchainService = null;

    static {
        try {
            blockchainService = BlockchainService.builder()
                                                 .minersNum(MINERS_NUM)
                                                 .clientsNum(CLIENTS_NUM)
                                                 .startDifficultLevel(START_DIFFICULT_LEVEL)
                                                 .maxNumBlockToMining(MAX_NUM_BLOCK_TO_MINING)
                                                 .rewardAmount(REWARD_AMOUNT)
                                                 .cryptocurrency(new VirtualCoin())
                                                 .startMinersAmount(START_MINERS_AMOUNT)
                                                 .startClientsAmount(START_CLIENTS_AMOUNT)
                                                 .build();
        } catch (NoSuchAlgorithmException | NegativeAmountException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        if (blockchainService != null) {
            blockchainService.start();

            System.out.println(blockchainService.getBlockchain());
        }
    }
}