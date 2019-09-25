package com.electroeing.designpatterns.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

enum EnumBased {
    INSATNCE;

    // Fields in enums are not serialized!!!!!!!!!!!
    private int value;

    EnumBased() {
        this.value = 42;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

public class EnumBasedSingleton {
    private static final Logger logger = LoggerFactory.getLogger(EnumBasedSingleton.class);
    static void saveToFile(EnumBased singleton, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(singleton);
        } catch (Exception e) {
            logger.error("Error saving to file", e);
        }
    }

    static EnumBased readFromFile(String filename) {
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (EnumBased) in.readObject();
        } catch (Exception e) {
            logger.error("Error getting from file", e);
            return null;
        }
    }

    public static void main(String[] args) {
        String filename = "myfile.bin";
        EnumBased singleton = EnumBased.INSATNCE;
        singleton.setValue(111);

        saveToFile(singleton, filename);

        EnumBased enumBased = readFromFile(filename);
        logger.info("Returned value from file: {}", enumBased.getValue());

    }
}
