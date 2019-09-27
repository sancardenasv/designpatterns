package com.electroeing.designpatterns.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

class Prop<T> {
    private static final Logger logger = LoggerFactory.getLogger(Prop.class);

    private T value;

    public Prop(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        // log here or control the variable assignation
        logger.info("THIS IS A CONTROLLED MESSAGE IN PROP WHEN SETTING VALUE");
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Prop<?> prop = (Prop<?>) obj;

        return Objects.equals(value, prop.value);
    }
}

class Creature {
    private Prop<Integer> agility = new Prop<>(10);


    public int getAgility() {
        return agility.getValue();
    }

    public void setAgility(int agility) {
        this.agility.setValue(agility);
    }
}

public class Property {
    public static void main(String[] args) {
        Creature creature = new Creature();
        creature.setAgility(12);
    }
}
