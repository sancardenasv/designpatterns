package com.electroeing.designpatterns.mediator;

import io.reactivex.Observable;
import io.reactivex.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class Broker extends Observable<Integer> {
    private List<Observer<? super Integer>> observers = new ArrayList<>();

    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        observers.add(observer);
    }

    public void publish(int n) {
        for(Observer<? super Integer> o : observers) {
            o.onNext(n);
        }
    }
}

class FootballPlayer {
    private int goalsScored = 0;
    private Broker broker;
    public String name;

    public FootballPlayer(Broker broker, String name) {
        this.broker = broker;
        this.name = name;
    }

    public void score() {
        broker.publish(++goalsScored);
    }
}

class FootballCoach {
    private final static Logger logger = LoggerFactory.getLogger(FootballCoach.class);

    public FootballCoach(Broker broker) {
        broker.subscribe(i -> {
            logger.info("Hey, you scored {} goals!", i);
        });
    }
}

public class EventBroker {
    public static void main(String[] args) {
        final Broker broker = new Broker();
        final FootballPlayer pablo = new FootballPlayer(broker, "pablo");
        final FootballCoach coach = new FootballCoach(broker);

        pablo.score();
        pablo.score();
        pablo.score();
    }
}
