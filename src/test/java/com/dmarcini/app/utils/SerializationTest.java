package com.dmarcini.app.utils;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

class SerializationTest {
    private final static String SERIALIZED_OBJECT_DIR = "./test-serialized-object/";
    private final static String SERIALIZED_OBJECT_NAME = "object.bin";

    private static TestClass testObject;

    @BeforeAll
    static void setUp() throws IOException {
        if (!new File(SERIALIZED_OBJECT_DIR).mkdir()) {
            throw new IOException("Cannot create dir!");
        }

        ArrayList<TestObjectClass> objects = new ArrayList<>(
                Arrays.asList(new TestObjectClass("lorem"),
                        new TestObjectClass("ipsum"),
                        new TestObjectClass("dolor"))
        );

        testObject = new TestClass(objects, "sit", 2020);
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.walk(Paths.get(SERIALIZED_OBJECT_DIR))
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
    }

    @Test
    public void serializeDeserialize_serializeDeserializeObject_succeed() throws IOException, ClassNotFoundException {
        Serialization.serialize(SERIALIZED_OBJECT_DIR + SERIALIZED_OBJECT_NAME, testObject);

        Optional<TestClass> optTestObject = Serialization.deserialize(SERIALIZED_OBJECT_DIR +
                                                                      SERIALIZED_OBJECT_NAME);

        Assertions.assertTrue(optTestObject.isPresent() && testObject.equals(optTestObject.get()));
    }

    static class TestObjectClass implements Serializable {
        private final String name;

        TestObjectClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class TestClass implements Serializable {
        private final List<TestObjectClass> objects;
        private final String text;
        private final int number;

        TestClass(ArrayList<TestObjectClass> objects, String text, int number) {
            this.objects = objects;
            this.text = text;
            this.number = number;
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

            if (SerializationTest.testObject.getObjects().size() != testClass.getObjects().size()) {
                return false;
            }

            for (int i = 0; i < SerializationTest.testObject.getObjects().size(); ++i) {
                if (!SerializationTest.testObject.getObjects().get(i).getName()
                                      .equals(testClass.getObjects().get(i).getName())) {
                    return false;
                }
            }

            return number == testClass.number && Objects.equals(text, testClass.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(objects, text, number);
        }

        public ArrayList<TestObjectClass> getObjects() {
            return (ArrayList<TestObjectClass>) objects;
        }

        public String getText() {
            return text;
        }

        public int getNumber() {
            return number;
        }
    }

}