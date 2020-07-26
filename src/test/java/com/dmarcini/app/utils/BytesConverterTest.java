package com.dmarcini.app.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BytesConverterTest {
    @Test
    void toBytesFromBytes_ConvertToBytesAndFromBytes_Succeed() throws IOException, ClassNotFoundException {
        TestObject testObject = new TestObject("Lorem ipsum", 2020, 3.14);

        byte[] bytes = BytesConverter.toBytes(testObject);

        assertEquals(testObject, BytesConverter.fromBytes(bytes));
    }

    static class TestObject implements Serializable {
        String text;
        long longNum;
        double doubleNum;

        TestObject(String text, long longNum, double doubleNum) {
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

            TestObject testObject = (TestObject) o;

            return longNum == testObject.longNum &&
                   Double.compare(testObject.doubleNum, doubleNum) == 0 &&
                   Objects.equals(text, testObject.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text, longNum, doubleNum);
        }
    }
}