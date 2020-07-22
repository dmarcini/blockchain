package com.dmarcini.app.stringutil;

import com.dmarcini.app.stringutil.StringUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StringUtilTest {

    @Test
    void applySHA256_SameHash_True() {
        String str1 = "Lorem ipsum";
        String str2 = "Lorem ipsum";

        assertEquals(StringUtil.applySHA256(str1), StringUtil.applySHA256(str2));
    }

    @Test
    void applySHA256_NotSameHash_False() {
        String str1 = "Lorem ipsum";
        String str2 = "Dolor sit amet";

        assertNotEquals(StringUtil.applySHA256(str1), StringUtil.applySHA256(str2));
    }
}