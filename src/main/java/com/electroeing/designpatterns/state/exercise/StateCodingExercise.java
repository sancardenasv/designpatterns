package com.electroeing.designpatterns.state.exercise;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

enum LockState {
    LOCKED,
    OPEN,
    ERROR
}

class CombinationLock {
    private static final Logger logger = LoggerFactory.getLogger(CombinationLock.class);

    private int[] combination;
    private String status;
    private int digitsLoaded = 0;

    public CombinationLock(int[] combination) {
        this.combination = combination;
        this.status = LockState.LOCKED.name();
        this.digitsLoaded = 0;
    }

    public void enterDigit(int digit) {
        // check digit and update status here
        logger.info("Current status: {}", this.status);

        if (status.equals(LockState.OPEN.name()) || status.equals(LockState.ERROR.name())) {
            logger.error("Already Finished!!!!!");
        } else {
            combination[digitsLoaded] -= digit;
            digitsLoaded++;
            if (combination.length == digitsLoaded) {
                validateCombination();
            } else {
                status = digitsLoaded == 1 ? String.valueOf(digit) : status.concat(String.valueOf(digit));
            }
        }
    }

    private void validateCombination() {
        this.status = LockState.OPEN.name();
        for (int i : combination) {
            if (i != 0) {
                this.status = LockState.ERROR.name();
                break;
            }
        }
        logger.info("Final status: {}", this.status);
    }

    public String getStatus() {
        return status;
    }
}

public class StateCodingExercise {
    @Test
    public void test() {
        final CombinationLock lock = new CombinationLock(new int[]{1, 2, 3});
        Assertions.assertEquals(LockState.LOCKED.name(), lock.getStatus());
        lock.enterDigit(1);
        Assertions.assertEquals("1", lock.getStatus());
        lock.enterDigit(2);
        Assertions.assertEquals("12", lock.getStatus());
        lock.enterDigit(3);
        Assertions.assertEquals(LockState.OPEN.name(), lock.getStatus());
    }

    @Test
    public void errorTest() {
        final CombinationLock lock = new CombinationLock(new int[]{1, 2, 8, 8});
        Assertions.assertEquals(LockState.LOCKED.name(), lock.getStatus());
        lock.enterDigit(1);
        Assertions.assertEquals("1", lock.getStatus());
        lock.enterDigit(2);
        Assertions.assertEquals("12", lock.getStatus());
        lock.enterDigit(8);
        Assertions.assertEquals("128", lock.getStatus());
        lock.enterDigit(5);
        Assertions.assertEquals(LockState.ERROR.name(), lock.getStatus());
    }
}
