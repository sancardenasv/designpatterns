package com.electroeing.designpatterns.memento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BankMemento {
    private int balance;

    public BankMemento(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }
}

class BankAccount {
    private int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    public BankMemento deposit(int amount) {
        balance += amount;
        return new BankMemento(balance);
    }

    public void restore(BankMemento m) {
        balance = m.getBalance();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BankAccount{");
        sb.append("balance=").append(balance);
        sb.append('}');
        return sb.toString();
    }
}

public class Memento {
    private static final Logger logger = LoggerFactory.getLogger(Memento.class);

    public static void main(String[] args) {
        final BankAccount ba = new BankAccount(100);

        final BankMemento m1 = ba.deposit(50);
        final BankMemento m2 = ba.deposit(25);
        logger.info("{}", ba);

        // restore to m1
        ba.restore(m1);
        logger.info("restored to m1: {}", ba);

        // restore to m2
        ba.restore(m2);
        logger.info("restored to m2: {}", ba);

    }
}
