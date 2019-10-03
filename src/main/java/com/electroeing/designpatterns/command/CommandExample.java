package com.electroeing.designpatterns.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class BankAccount {
    private final static Logger logger = LoggerFactory.getLogger(BankAccount.class);

    private int balance;
    private int overdraftLimit = -500;

    public boolean deposit(int amount) {
        balance += amount;
        logger.info("Deposited {}, new balance: {}", amount, balance);
        return true;
    }

    public boolean withdraw(int amount) {
        if (balance - amount >= overdraftLimit) {
            balance -= amount;
            logger.info("Withdrew {}, new balance: {}", amount, balance);
            return true;
        }
        logger.info("Can nor Withdraw! Not enough fonds");
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BankAccount{");
        sb.append("balance=").append(balance);
        sb.append('}');
        return sb.toString();
    }
}

interface Command {
    void call();
    void undo();
}

class BankAccountCommand implements Command {
    private BankAccount account;
    private Action action;
    private int amount;
    private boolean success;

    public BankAccountCommand(BankAccount account, Action action, int amount) {
        this.account = account;
        this.action = action;
        this.amount = amount;
    }

    @Override
    public void call() {
        switch (action) {
            case DEPOSIT:
                success = account.deposit(amount);
                break;
            case WITHDRAW:
                success = account.withdraw(amount);
                break;
        }
    }

    @Override
    public void undo() {
        if (!success) return;
        switch (action) {
            case DEPOSIT:
                account.withdraw(amount);
                break;
            case WITHDRAW:
                account.deposit(amount);
                break;
        }
    }

    public enum Action {
        DEPOSIT, WITHDRAW
    }
}

public class CommandExample {
    private static final Logger logger = LoggerFactory.getLogger(CommandExample.class);

    public static void main(String[] args) {
        final BankAccount ba = new BankAccount();
        logger.info("Created bank account - {}", ba);

        final List<BankAccountCommand> commands = Arrays.asList(
                new BankAccountCommand(ba, BankAccountCommand.Action.DEPOSIT, 100),
                new BankAccountCommand(ba, BankAccountCommand.Action.WITHDRAW, 1000)
        );

        for (BankAccountCommand c : commands) {
            c.call();
            logger.info("Account after action - {}", ba);
        }

        Collections.reverse(commands);
        for (BankAccountCommand c : commands) {
            c.undo();
            logger.info("Account after action - {}", ba);
        }
    }
}
