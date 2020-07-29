package com.dmarcini.app.utils.cryptography;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HashGeneratorTest {
    @Test
    void applySHA256_SameHash_True() {
        assertEquals(HashGenerator.applySHA256("Lorem ipsum"),
                     HashGenerator.applySHA256("Lorem ipsum"));
    }

    @Test
    void applySHA256_NotSameHash_False() {
        assertNotEquals(HashGenerator.applySHA256("Lorem ipsum"),
                        HashGenerator.applySHA256("Dolor sit amet"));
    }
}
