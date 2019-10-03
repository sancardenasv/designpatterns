package com.electroeing.designpatterns.command.exercise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Command
{
    enum Action
    {
        DEPOSIT, WITHDRAW
    }

    public Action action;
    public int amount;
    public boolean success;

    public Command(Action action, int amount)
    {
        this.action = action;
        this.amount = amount;
    }
}

class Account
{
    public int balance;

    public void process(Command c)
    {
        switch (c.action) {
            case WITHDRAW:
                if (balance >= c.amount) {
                    balance -= c.amount;
                    c.success = true;
                } else {
                    c.success = false;
                }
                break;
            case DEPOSIT:
                balance += c.amount;
                c.success = true;
                break;
        }
    }
}

public class CommandCodingExercise {
    @Test
    public void test()
    {
        Account a = new Account();

        Command command = new Command(Command.Action.DEPOSIT, 100);
        a.process(command);

        assertEquals(100, a.balance);
        assertTrue(command.success);

        command = new Command(Command.Action.WITHDRAW, 50);
        a.process(command);

        assertEquals(50, a.balance);
        assertTrue(command.success);

        command = new Command(Command.Action.WITHDRAW, 150);
        a.process(command);

        assertEquals(50, a.balance);
        assertFalse(command.success);
    }
}
