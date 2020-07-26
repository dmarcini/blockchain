package com.dmarcini.app.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BytesConverterTest {
    @Test
    void toBytesFromBytes_convertToBytesArrayAndFromBytes_succeed() throws IOException, ClassNotFoundException {
        TestClass testObject = new TestClass("Lorem ipsum", 2020, 3.14);

        byte[] bytes = BytesConverter.toBytes(testObject);

        assertEquals(testObject, BytesConverter.fromBytes(bytes));
    }

    static class TestClass implements Serializable {
        String text;
        long longNum;
        double doubleNum;

        TestClass(String text, long longNum, double doubleNum) {
            this.text = text;
            this.longNum = longNum;
            this.doubleNum = doubleNum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TestClass testClass = (TestClass) o;

            return longNum == testClass.longNum &&
                   Double.compare(testClass.doubleNum, doubleNum) == 0 &&
                   Objects.equals(text, testClass.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text, longNum, doubleNum);
        }
    }
}