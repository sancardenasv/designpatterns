package com.electroeing.designpatterns.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class SingletonClass implements Serializable {
    private static final SingletonClass INSTANCE = new SingletonClass();
    private int value = 0;

    private SingletonClass() {

    }

    public static SingletonClass getInstance() {
        return INSTANCE;
    }

    protected Object readResolve() {
        return INSTANCE;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

public class BasicSingleton {
    private static final Logger logger = LoggerFactory.getLogger(BasicSingleton.class);

    private static void saveToFile(SingletonClass singleton, String filename) throws IOException {

        try (FileOutputStream fileOutputStream = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOutputStream)) {
            out.writeObject(singleton);
        }
    }

    private static SingletonClass readFromFile(String filename) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (SingletonClass) in.readObject();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SingletonClass singleton = SingletonClass.getInstance();

        singleton.setValue(13);

        logger.info("Singleton Value: {}", singleton.getValue());

        /*
        Problems:
        1. reflection
        2. serialization (fixed with "readResolve")
         */

        String filename = "singleton.bin";
        saveToFile(singleton, filename);

        singleton.setValue(222);

        SingletonClass singletonClass = readFromFile(filename);

        logger.info("Singleton equals: {}", singleton.equals(singletonClass));
        logger.info("Original singleton: {}", singleton.getValue());
        logger.info("Read singleton: {}", singletonClass.getValue());
    }
}
