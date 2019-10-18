package com.electroeing.designpatterns.template.exercise;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Creature {
    public int attack, health;

    public Creature(int attack, int health) {
        this.attack = attack;
        this.health = health;
    }
}

abstract class CardGame {
    public Creature[] creatures;

    public CardGame(Creature[] creatures) {
        this.creatures = creatures;
    }

    // returns -1 if no clear winner (both alive or both dead)
    public int combat(int creature1, int creature2) {
        Creature first = creatures[creature1];
        Creature second = creatures[creature2];
        hit(first, second);
        hit(second, first);

        int result;
        if ((first.health <= 0 && second.health <= 0) || (first.health > 0 && second.health > 0)) {
            result = -1;
        } else if (first.health <= 0) {
            result = creature2;
        } else {
            result = creature1;
        }
        endTurn();
        return result;
    }

    // attacker hits other creature
    protected abstract void hit(Creature attacker, Creature other);

    protected abstract void endTurn();
}

class TemporaryCardDamageGame extends CardGame {
    private Map<Creature, Integer> creaturesHealth = new HashMap<>();
    
    public TemporaryCardDamageGame(Creature[] creatures) {
        super(creatures);
    }

    @Override
    protected void hit(Creature attacker, Creature other) {
        creaturesHealth.put(other, other.health);
        other.health -= attacker.attack;
    }

    @Override
    protected void endTurn() {
        creaturesHealth.forEach((c, h) -> {
            c.health = h;
        });
    }
}

class PermanentCardDamageGame extends CardGame {

    public PermanentCardDamageGame(Creature[] creatures) {
        super(creatures);
    }

    @Override
    protected void hit(Creature attacker, Creature other) {
        other.health -= attacker.attack;
    }

    @Override
    protected void endTurn() {
        // Do nothing
    }
}

public class TemplateCodingExercise {
    @Test
    public void impasseTest()
    {
        Creature c1 = new Creature(1, 2);
        Creature c2 = new Creature(1, 2);
        TemporaryCardDamageGame game = new TemporaryCardDamageGame(new Creature[]{c1, c2});
        assertEquals(-1, game.combat(0,1));
        assertEquals(-1, game.combat(0,1));
    }

    @Test
    public void temporaryMurderTest()
    {
        Creature c1 = new Creature(1, 1);
        Creature c2 = new Creature(2, 2);
        TemporaryCardDamageGame game = new TemporaryCardDamageGame(new Creature[]{c1, c2});
        assertEquals(1, game.combat(0,1));
    }

    @Test
    public void doubleMurderTest()
    {
        Creature c1 = new Creature(2, 2);
        Creature c2 = new Creature(2, 2);
        TemporaryCardDamageGame game = new TemporaryCardDamageGame(new Creature[]{c1, c2});
        assertEquals(-1, game.combat(0,1));
    }

    @Test
    public void permanentDamageDeathTest()
    {
        Creature c1 = new Creature(1, 2);
        Creature c2 = new Creature(1, 3);
        CardGame game = new PermanentCardDamageGame(new Creature[]{c1, c2});
        assertEquals(-1, game.combat(0,1));
        assertEquals(1, game.combat(0,1));
    }
}
