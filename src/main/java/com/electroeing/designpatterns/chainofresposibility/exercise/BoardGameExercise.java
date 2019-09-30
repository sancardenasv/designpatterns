package com.electroeing.designpatterns.chainofresposibility.exercise;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// Query listener
class Event<A> {
    private int index;
    private Map<Integer, Consumer<A>> handlers = new HashMap<>();

    public int subscribe(Consumer<A> handler) {
        int i = index;
        handlers.put(index++, handler);
        return i;
    }

    public void unsubscribe(int key) {
        handlers.remove(key);
    }

    public void fire(A args) {
        for (Consumer<A> handler : handlers.values()) {
            handler.accept(args);
        }
    }
}

class Query {
    public String entityName;
    public Statistic statistic;
    public int result;

    public Query(String entityName, Statistic statistic, int result) {
        this.entityName = entityName;
        this.statistic = statistic;
        this.result = result;
    }
}

abstract class Creature {
    protected Game game;
    public String name;
    public int baseAttack;
    public int baseDefense;

    public abstract int getAttack();
    public abstract int getDefense();
}

class Goblin extends Creature {

    public Goblin(Game game)
    {
        this.game = game;
        this.name = "goblin";
        this.baseAttack = 1;
        this.baseDefense = 1;
    }

    @Override
    public int getAttack()
    {
        try (final AttackModifier attackModifier = new AttackModifier(game, this)) {
            Query query = new Query(name, Statistic.ATTACK, baseAttack);
            game.queries.fire(query);
            return query.result;
        }
    }

    @Override
    public int getDefense()
    {
        try (final DefenseModifier defenseModifier = new DefenseModifier(game, this)) {
            Query query = new Query(name, Statistic.DEFENSE, baseDefense);
            game.queries.fire(query);
            return query.result;
        }
    }
}

class GoblinKing extends Goblin {
    public GoblinKing(Game game) {
        super(game);
        this.name = "Goblin King";
        this.baseAttack = 3;
        this.baseDefense = 3;
    }
}

enum Statistic {
    ATTACK, DEFENSE
}

class EntityModifier {
    protected Game game;
    protected Creature creature;

    public EntityModifier(Game game, Creature creature) {
        this.game = game;
        this.creature = creature;
    }
}

class AttackModifier extends EntityModifier implements AutoCloseable {
    private final int token;

    public AttackModifier(Game game, Creature creature) {
        super(game, creature);
        token = game.queries.subscribe(query -> {
            if (query.entityName.equals(creature.name) && !"Goblin King".equals(creature.name) && query.statistic == Statistic.ATTACK) {
                game.creatures.forEach(cr -> {
                    if ("Goblin King".equals(cr.name)) {
                        query.result += 1;
                    }
                });
            }
        });
    }

    @Override
    public void close() {
        game.queries.unsubscribe(token);
    }
}

class DefenseModifier extends EntityModifier implements AutoCloseable {
    private final int token;

    public DefenseModifier(Game game, Creature creature) {
        super(game, creature);
        token = game.queries.subscribe(query -> {
            if (query.entityName.equals(creature.name) && query.statistic == Statistic.DEFENSE) {
                query.result = query.result + game.creatures.size() - 1;
            }
        });
    }

    @Override
    public void close() {
        game.queries.unsubscribe(token);
    }
}

class Game {
    public Event<Query> queries = new Event<>();
    public List<Creature> creatures = new ArrayList<>();
}

public class BoardGameExercise {
    private Game game;

    @BeforeEach
    public void startup() {
        game = new Game();
    }

    @Test
    public void onlyOneGoblinTest() {
        final Goblin goblin = new Goblin(game);
        game.creatures.add(goblin);
        Assertions.assertEquals(1, goblin.getAttack());
        Assertions.assertEquals(1, goblin.getDefense());
    }

    @Test
    public void twoGoblinTest() {
        final Goblin goblin1 = new Goblin(game);
        final Goblin goblin2 = new Goblin(game);
        game.creatures.add(goblin1);
        game.creatures.add(goblin2);
        Assertions.assertEquals(1, goblin1.getAttack());
        Assertions.assertEquals(2, goblin1.getDefense());
    }

    @Test
    public void twoGoblinAndOneKingTest() {
        final Goblin goblin1 = new Goblin(game);
        final Goblin goblin2 = new Goblin(game);
        final GoblinKing goblinKing = new GoblinKing(game);
        game.creatures.add(goblin1);
        game.creatures.add(goblin2);
        game.creatures.add(goblinKing);
        Assertions.assertEquals(2, goblin1.getAttack());
        Assertions.assertEquals(3, goblin1.getDefense());
        Assertions.assertEquals(2, goblin2.getAttack());
        Assertions.assertEquals(3, goblin2.getDefense());
        Assertions.assertEquals(3, goblinKing.getAttack());
        Assertions.assertEquals(5, goblinKing.getDefense());
    }

}
