package com.electroeing.designpatterns.observer.exercise;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Imagine a game where one or more rats attack a player. Each individual rat has an attack value of 1.
 * However, rats attack as a swarm, so each rat's attack value is equal to the total number of rats in play.
 * <p>
 * Given that a rat enters a play through the constructor and leaves (dies) via its close() method.
 */

class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    public Event<AttackChangeEventArgs> swarmChange = new Event<>();
    public List<Rat> swarm = new ArrayList<>();

    public Game() {
        swarmChange.addHandler(h -> {
            for (Rat rat : swarm) {
                rat.attack = h.attackModifier;
                logger.info("Rat attack modified to {}", rat.attack);
            }
        });
    }
}

class Rat implements Closeable {

    private Game game;
    public int attack = 1;

    /**
     * rat enters game here
     * @param game:
     */
    public Rat(Game game) {
        this.game = game;
        game.swarm.add(this);
        game.swarmChange.fire(new AttackChangeEventArgs(game, game.swarm.size()));
    }

    /**
     * rat dies ;(u
     */
    @Override
    public void close() {
        game.swarm.remove(this);
        game.swarmChange.fire(new AttackChangeEventArgs(game, game.swarm.size()));
    }
}

class Event<T> {
    private int count = 0;
    private Map<Integer, Consumer<T>> handlers = new HashMap<>();

    public Subscription addHandler(Consumer<T> handler) {
        int i = count;
        handlers.put(count++, handler);
        return new Subscription(this, i);
    }

    public void fire(T args) {
        for (Consumer<T> handler : handlers.values()) {
            handler.accept(args);
        }
    }

    public class Subscription implements AutoCloseable {
        private Event<T> event;
        private int id;

        public Subscription(Event<T> event, int id) {
            this.event = event;
            this.id = id;
        }

        @Override
        public void close() {
            event.handlers.remove(id);
        }
    }
}

class AttackChangeEventArgs {
    public Object source;
    public int attackModifier;

    public AttackChangeEventArgs(Object source, int attackModifier) {
        this.source = source;
        this.attackModifier = attackModifier;
    }
}

public class ObservableCodingExercise {
    @Test
    public void test() {
        final Game game = new Game();

        final Rat rat1 = new Rat(game);
        final Rat rat2 = new Rat(game);
        verifyAttack(game, 2);

        rat1.close();
        verifyAttack(game, 1);
    }

    private void verifyAttack(Game game, int attack) {
        for (Rat rat :
                game.swarm) {
            Assertions.assertEquals(attack, rat.attack);
        }
    }
}
