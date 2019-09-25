package com.electroeing.designpatterns.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

class StaticBlockSingletonClass {
    private static final Logger logger = LoggerFactory.getLogger(StaticBlockSingletonClass.class);

    private static StaticBlockSingletonClass instance;
    private String value;

    public StaticBlockSingletonClass() throws IOException {
        logger.info("Singleton initializing");
        File.createTempFile(".",".");
    }

    static {
        try {
            instance = new StaticBlockSingletonClass();
        } catch (Exception e) {
            logger.error("Failed to create singleton", e);
        }
    }

    public static StaticBlockSingletonClass getInstance() {
        return instance;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StaticBlockSingletonClass{");
        sb.append("value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

public class StaticBlockSingleton {
    private static final Logger logger = LoggerFactory.getLogger(StaticBlockSingleton.class);
    public static void main(String[] args) {
        StaticBlockSingletonClass singleton = StaticBlockSingletonClass.getInstance();
        logger.info("Instance {}", singleton);

        singleton.setValue("Hello");
        logger.info("Instance after modification {}", singleton);


    }


}
