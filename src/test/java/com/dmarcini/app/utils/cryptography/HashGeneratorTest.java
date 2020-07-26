package com.dmarcini.app.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HashGeneratorTest {

    @Test
    void applySHA256_SameHash_True() {
        String str1 = "Lorem ipsum";
        String str2 = "Lorem ipsum";

        assertEquals(HashGenerator.applySHA256(str1), HashGenerator.applySHA256(str2));
    }

    @Test
    void applySHA256_NotSameHash_False() {
        String str1 = "Lorem ipsum";
        String str2 = "Dolor sit amet";

        assertNotEquals(HashGenerator.applySHA256(str1), HashGenerator.applySHA256(str2));
    }
}