package com.electroeing.designpatterns.iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;

class Creature implements Iterable<Integer> {
    private enum stat{
        STRENGTH(0),
        AGILITY(1),
        INTELLIGENCE(2);

        private final int pos;

        stat(int pos) {
            this.pos = pos;
        }

        public int getPos() {
            return pos;
        }
    }
    private int[] stats = new int[stat.values().length];

    public int getStrength() {
        return stats[stat.STRENGTH.getPos()];
    }

    public void setStrength(int value) {
        stats[stat.STRENGTH.getPos()] = value;
    }

    public int getAgility() {
        return stats[stat.AGILITY.getPos()];
    }

    public void setAgility(int value) {
        stats[stat.AGILITY.getPos()] = value;
    }

    public int getIntelligence() {
        return stats[stat.INTELLIGENCE.getPos()];
    }

    public void setIntelligence(int value) {
        stats[stat.INTELLIGENCE.getPos()] = value;
    }

    public int sum() {
        return IntStream.of(stats).sum();
    }

    public int max() {
        return IntStream.of(stats).max().orElse(0);
    }

    public double average() {
        return IntStream.of(stats).average().orElse(0);
    }

    @Override
    public Iterator<Integer> iterator() {
        return IntStream.of(stats).iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        IntStream.of(stats).forEach(action::accept);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return IntStream.of(stats).spliterator();
    }
}

public class ArrayBackedProperties {
    private final static Logger logger = LoggerFactory.getLogger(ArrayBackedProperties.class);

    public static void main(String[] args) {
        final Creature creature = new Creature();
        creature.setAgility(12);
        creature.setIntelligence(13);
        creature.setStrength(17);

        logger.info("Creature max stat is {} - total stats {} - average stats {}",
                creature.max(), creature.sum(), creature.average());
    }
}
