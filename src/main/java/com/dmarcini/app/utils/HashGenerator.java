package com.dmarcini.app.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashGenerator {
    public static String applySHA256(String input) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                                       .digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte element : hash) {
                String hex = Integer.toHexString(0xff & element);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
