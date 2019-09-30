package com.electroeing.designpatterns.chainOfResposibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Creature {
    public String name;
    public int attack, defense;

    public Creature(String name, int attack, int defense) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Creature{");
        sb.append("name='").append(name).append('\'');
        sb.append(", attack=").append(attack);
        sb.append(", defense=").append(defense);
        sb.append('}');
        return sb.toString();
    }
}

class CreatureModifier {
    private final static Logger logger = LoggerFactory.getLogger(CreatureModifier.class);

    protected Creature creature;
    protected CreatureModifier nextMod;

    public CreatureModifier(Creature creature) {
        this.creature = creature;
    }

    public void addMod(CreatureModifier cm) {
        if (nextMod != null) {
            nextMod.addMod(cm);
        } else {
            logger.info("{} encountered an event that will {}", creature.name, cm.getClass().getSimpleName());
            nextMod = cm;
        }
    }

    public void handle() {
        if (nextMod != null) {
            nextMod.handle();
        }
    }
}

class DoubleAttackModifier extends CreatureModifier {
    private static final Logger logger = LoggerFactory.getLogger(DoubleAttackModifier.class);

    public DoubleAttackModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle() {
        logger.info("Doubling {}'s attack", creature.name);
        creature.attack *= 2;
        super.handle();
    }
}

class IncreaseDefenseModifier extends CreatureModifier {
    private static final Logger logger = LoggerFactory.getLogger(IncreaseDefenseModifier.class);


    public IncreaseDefenseModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle() {
        logger.info("Increasing {}'s defense by 3", creature.name);
        creature.defense += 3;
        super.handle();
    }
}

class NoBonusesModifier extends CreatureModifier {
    private static final Logger logger = LoggerFactory.getLogger(NoBonusesModifier.class);

    public NoBonusesModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle() {
        logger.warn("No more bonuses can be applied due to a curse!!!");
    }
}

public class MethodChain {
    private static final Logger logger = LoggerFactory.getLogger(MethodChain.class);

    public static void main(String[] args) {
        Creature goblin = new Creature("Goblin", 2, 2);
        logger.info(goblin.toString());

        CreatureModifier root = new CreatureModifier(goblin);
        root.addMod(new DoubleAttackModifier(goblin));
        root.addMod(new NoBonusesModifier(goblin));
        root.addMod(new IncreaseDefenseModifier(goblin));
        root.handle();
        logger.info(goblin.toString());
    }
}
