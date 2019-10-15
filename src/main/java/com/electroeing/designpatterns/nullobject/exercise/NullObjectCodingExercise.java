package com.electroeing.designpatterns.nullobject.exercise;

import org.junit.jupiter.api.Test;

interface Log {
    // max # of elements in the log
    int getRecordLimit();

    // number of elements already in the log
    int getRecordCount();

    // expected to increment record count
    void logInfo(String message);
}

class Account {
    private Log log;

    public Account(Log log) {
        this.log = log;
    }

    public void someOperation() throws Exception {
        int c = log.getRecordCount();
        log.logInfo("Performing an operation");
        if (c + 1 != log.getRecordCount())
            throw new Exception();
        if (log.getRecordCount() >= log.getRecordLimit())
            throw new Exception();
    }
}

class NullLog implements Log {
    private int limit = 1;
    private int count = 0;

    @Override
    public int getRecordLimit() {
        return limit;
    }

    @Override
    public int getRecordCount() {
        limit ++;
        return count ++;
    }

    @Override
    public void logInfo(String message) {

    }
}

public class NullObjectCodingExercise {
    @Test
    public void singleCallTest() throws Exception
    {
        Account a = new Account(new NullLog());
        a.someOperation();
    }

    @Test
    public void manyCallsTest() throws Exception
    {
        Account a = new Account(new NullLog());
        for (int i = 0; i < 100; ++i)
            a.someOperation();
    }
}
