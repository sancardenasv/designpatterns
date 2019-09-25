package com.electroeing.designpatterns.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

enum Subsystem{
    PRIMARY,
    AUXILIARY,
    FALLBACK;
}

class Printer {
    private static final Logger logger = LoggerFactory.getLogger(Printer.class);

    private  static HashMap<Subsystem, Printer> instances = new HashMap<>();

    private Printer() {
        logger.info("A total of {} printers have been initialized", instances.size() + 1);
    }

    public static Printer get(Subsystem ss) {
        if (instances.containsKey(ss)) {
            return instances.get(ss);
        }

        Printer printer = new Printer();
        instances.put(ss, printer);
        return printer;
    }
}

public class MultitonSingleton {
    private static final Logger logger = LoggerFactory.getLogger(MultitonSingleton.class);

    public static void main(String[] args) {
        Printer main = Printer.get(Subsystem.PRIMARY);
        Printer aux = Printer.get(Subsystem.AUXILIARY);
        Printer aux2 = Printer.get(Subsystem.AUXILIARY);
    }
}
