package com.electroeing.designpatterns.chainofresposibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
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
    enum Argument {
        ATTACK, DEFENSE
    }
    public Argument argument;
    public int result;

    public Query(String entityName, Argument argument, int result) {
        this.entityName = entityName;
        this.argument = argument;
        this.result = result;
    }
}

class Game {
    public Event<Query> queries = new Event<>();
}

class Entity {
    private Game game;
    public String name;
    public int baseAttack, baseDefense;

    public Entity(Game game, String name, int baseAttack, int baseDefense) {
        this.game = game;
        this.name = name;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
    }

    public int getAttack() {
        Query query = new Query(name, Query.Argument.ATTACK, baseAttack);
        game.queries.fire(query);
        return query.result;
    }

    public int getDefense() {
        Query query = new Query(name, Query.Argument.DEFENSE, baseDefense);
        game.queries.fire(query);
        return query.result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Entity{");
        sb.append("name='").append(name).append('\'');
        sb.append(", attack=").append(getAttack());
        sb.append(", defense=").append(getDefense());
        sb.append('}');
        return sb.toString();
    }
}

class EntityModifier {
    protected Game game;
    protected Entity entity;

    public EntityModifier(Game game, Entity entity) {
        this.game = game;
        this.entity = entity;
    }
}

class TripleAttackModifier extends EntityModifier implements AutoCloseable {
    private final int token;

    public TripleAttackModifier(Game game, Entity entity) {
        super(game, entity);
        token = game.queries.subscribe(query -> {
            if (query.entityName.equals(entity.name) && query.argument == Query.Argument.ATTACK) {
                query.result *= 3;
            }
        });
    }

    @Override
    public void close() {
        game.queries.unsubscribe(token);
    }
}

class DecreaseDefenseModifier extends EntityModifier {
    private final int token;

    public DecreaseDefenseModifier(Game game, Entity entity) {
        super(game, entity);
        token = game.queries.subscribe(query -> {
            if (query.entityName.equals(entity.name) && query.argument == Query.Argument.DEFENSE) {
                query.result -= 1;
            }
        });
    }
}

public class BrokerChain {
    private final static Logger logger = LoggerFactory.getLogger(BrokerChain.class);

    public static void main(String[] args) {
        Game game = new Game();
        Entity strongGoblin = new Entity(game, "Strong Goblin", 2, 2);
        logger.info("Created new Entity: {}", strongGoblin);

        DecreaseDefenseModifier ddm = new DecreaseDefenseModifier(game, strongGoblin);

        try (final TripleAttackModifier tam = new TripleAttackModifier(game, strongGoblin)) {
            logger.info(strongGoblin.toString());
        }

        logger.info("END: {}", strongGoblin);

    }
}
