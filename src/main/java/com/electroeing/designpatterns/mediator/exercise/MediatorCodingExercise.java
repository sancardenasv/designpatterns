package com.electroeing.designpatterns.mediator.exercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class Participant {
    public int value;
    private Mediator mediator;

    public Participant(Mediator mediator) {
        this.value = 0;
        this.mediator = mediator;
        mediator.subscribe(this);
    }

    public void say(int n) {
        mediator.broadcast(this, n);
    }

    public void increaseValue(int value) {
        this.value += value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Participant{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}

class Mediator {
    private List<Participant> participants = new ArrayList<>();

    public void subscribe(Participant p) {
        participants.add(p);
    }

    public void broadcast(Participant participant, int value) {
        participants.stream()
                .filter(p -> !p.equals(participant))
                .forEach(p -> p.increaseValue(value));
    }
}

public class MediatorCodingExercise {
    private final static Logger logger = LoggerFactory.getLogger(MediatorCodingExercise.class);

    public static void main(String[] args) {
        final Mediator mediator = new Mediator();

        final Participant participant1 = new Participant(mediator);
        final Participant participant2 = new Participant(mediator);

        logger.info("Participant1 - {}", participant1);
        logger.info("Participant2 - {}", participant2);
        logger.info("----------------------------------");

        participant1.say(3);
        logger.info("Participant1 - {}", participant1);
        logger.info("Participant2 - {}", participant2);
        logger.info("----------------------------------");

        participant2.say(2);
        logger.info("Participant1 - {}", participant1);
        logger.info("Participant2 - {}", participant2);
        logger.info("----------------------------------");

    }
}
