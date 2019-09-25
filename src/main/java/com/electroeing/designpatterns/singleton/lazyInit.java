package com.electroeing.designpatterns.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LazySingleton {
    private static final Logger logger = LoggerFactory.getLogger(LazySingleton.class);

    private static LazySingleton instance;

    private LazySingleton() {
        logger.info("Initializing a lazy singleton");
    }

    /* NOT thread safe
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }

        return instance;
    }*/

    // Double checked locking (outdated, better not to use)
    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }

        return instance;
    }
}
public class lazyInit {
}
