package com.electroeing.designpatterns.nullobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

interface Log {
    void info(String msg);
    void warn(String msg);
    void error(String msg);
}

class ConsoleLog implements Log {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleLog.class);

    @Override
    public void info(String msg) {
        logger.info("THIS IS A INFO: {}", msg);
    }

    @Override
    public void warn(String msg) {
        logger.warn("THIS IS A WARN: {}", msg);
    }

    @Override
    public void error(String msg) {
        logger.error("THIS IS A ERROR: {}", msg);
    }
}

class BankAccount {
    private Log log;
    private int balance;

    public BankAccount(Log log) {
        this.log = log;
    }

    public void deposit(int amount) {
        balance += amount;

        log.info("Deposited " + amount);
    }
}

final class NullLog implements Log {

    @Override
    public void info(String msg) {

    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void error(String msg) {

    }
}

public class NullObject {
    /** Dynamic Null Object **/
    @SuppressWarnings("unchecked")
    public static <T> T noOp(Class<T> itf) {
        return (T) Proxy.newProxyInstance(
                itf.getClassLoader(),
                new Class<?>[] { itf },
                (proxy, method, args) -> {
                    if (method.getReturnType().equals(Void.TYPE)) {
                        return null;
                    }
                    else {
                        return method.getReturnType().getConstructor().newInstance();
                    }
                });
    }

     public static void main(String[] args) {
        /* with logging:
        final Log log = new ConsoleLog();
         */
        /* NO LOGGING (Do not use null as param)
        final Log log = new NullLog();*/

        Log log = noOp(Log.class);
        final BankAccount account = new BankAccount(log);

        account.deposit(100);
    }
}
