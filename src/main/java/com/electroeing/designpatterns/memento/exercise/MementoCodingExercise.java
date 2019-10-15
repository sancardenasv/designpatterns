package com.electroeing.designpatterns.memento.exercise;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Token {
    public int value = 0;

    public Token(int value) {
        this.value = value;
    }
}

class Memento {
    public List<Token> tokens = new ArrayList<>();
}

class TokenMachine {
    public List<Token> tokens = new ArrayList<>();

    public Memento addToken(int value) {
        final Token token = new Token(value);
        tokens.add(token);
        final Memento memento = new Memento();
        tokens.forEach(t -> memento.tokens.add(new Token(t.value)));
        return memento;
    }

    public Memento addToken(Token token) {
        tokens.add(token);
        final Memento memento = new Memento();
        tokens.forEach(t -> memento.tokens.add(new Token(t.value)));
        return memento;
    }

    public void revert(Memento m) {
        this.tokens = m.tokens;
    }
}

public class MementoCodingExercise {
    @Test
    public void singleTokenTest()
    {
        TokenMachine tm = new TokenMachine();
        Memento m = tm.addToken(123);
        tm.addToken(456);
        tm.revert(m);

        assertEquals(1,  tm.tokens.size());
        assertEquals(123, tm.tokens.get(0).value);
    }

    @Test
    public void twoTokenTest()
    {
        TokenMachine tm = new TokenMachine();
        tm.addToken(1);
        Memento m = tm.addToken(2);
        tm.addToken(3);
        tm.revert(m);
        assertEquals(2, tm.tokens.size());
        assertEquals(1, tm.tokens.get(0).value, "First token should have value 1");
        assertEquals(2, tm.tokens.get(1).value);
    }

    @Test
    public void fiddledTokenTest()
    {
        TokenMachine tm = new TokenMachine();
        System.out.println("Made a token with value 111 and kept a reference");
        Token token = new Token(111);
        System.out.println("Added this token to the list");
        tm.addToken(token);
        Memento m = tm.addToken(222);
        System.out.println("Changed this token's value to 333 :)");
        token.value = 333;
        tm.revert(m);

        assertEquals(
                2, tm.tokens.size(),
                "At this point, token machine should have exactly two tokens, "
                        + "but you got " + tm.tokens.size()
        );

        assertEquals(111, tm.tokens.get(0).value,
                "You got the token value wrong here. " +
                "Hint: did you init the memento by value?");
    }
}
