package com.dmarcini.app.utils;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class SerializationTest {
    private final static String SERIALIZED_OBJECT_DIR = "serialized-object/";
    private final static String SERIALIZED_OBJECT_NAME = "object.bin";

    private static SerializableObject testObject;

    @BeforeAll
    static void setUp() throws IOException {
        if (!new File(SERIALIZED_OBJECT_DIR).mkdir()) {
            throw new IOException("Cannot create dir!");
        }

        ArrayList<SerializableObject.TestObject> objects = new ArrayList<>(
                Arrays.asList(new SerializableObject.TestObject("lorem"),
                        new SerializableObject.TestObject("ipsum"),
                        new SerializableObject.TestObject("dolor"))
        );

        testObject = new SerializableObject(objects, "sit", 2020);
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.walk(Paths.get(SERIALIZED_OBJECT_DIR))
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
    }

    @Test
    public void serializeDeserialize_SerializeAndDeserializeObject_succeed() throws IOException,
                                                                                    ClassNotFoundException {
        Serialization.serialize(SERIALIZED_OBJECT_DIR + SERIALIZED_OBJECT_NAME, testObject);

        Optional<SerializableObject> optTestObject = Serialization.deserialize(SERIALIZED_OBJECT_DIR +
                                                                               SERIALIZED_OBJECT_NAME);

        Assertions.assertTrue(optTestObject.isPresent() && testObject.equals(optTestObject.get()));
    }

    static class SerializableObject implements Serializable {
        private final ArrayList<TestObject> objects;
        private final String text;
        private final int number;

        SerializableObject(ArrayList<TestObject> objects, String text, int number) {
            this.objects = objects;
            this.text = text;
            this.number = number;
        }

        public ArrayList<TestObject> getObjects() {
            return objects;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            SerializableObject serializableObject = (SerializableObject) o;

            if (objects.size() != serializableObject.getObjects().size()) {
                return false;
            }

            for (int i = 0; i < objects.size(); ++i) {
                if (!objects.get(i).getName().equals(serializableObject.getObjects().get(i).getName())) {
                    return false;
                }
            }

            return number == serializableObject.number && Objects.equals(text, serializableObject.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(objects, text, number);
        }

        private static class TestObject implements Serializable {
            String name;

            TestObject(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }
    }

}