package com.electroeing.designpatterns.decorator.exercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Bird {
    public int age;

    public String fly() {
        return age < 10 ? "flying" : "too old";
    }
}

class Lizard {
    public int age;

    public String crawl() {
        return (age > 1) ? "crawling" : "too young";
    }
}

class Dragon {
    private int age;
    private Bird bird;
    private Lizard lizard;

    public Dragon() {
        this.bird = new Bird();
        this.lizard = new Lizard();
    }

    public void setAge(int age) {
        this.age = age;
        bird.age = age;
        lizard.age = age;
    }

    public String fly() {
        return bird.fly();
    }

    public String crawl() {
        return lizard.crawl();
    }
}

public class DecoratorCodingExercise {
    private static final Logger logger = LoggerFactory.getLogger(DecoratorCodingExercise.class);

    public static void main(String[] args) {
        Dragon dragon = new Dragon();
        dragon.setAge(100);

        logger.info(dragon.fly());
        logger.info(dragon.crawl());
    }
}
