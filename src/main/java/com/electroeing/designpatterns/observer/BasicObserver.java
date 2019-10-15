package com.electroeing.designpatterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class PropertyChangedEventArgs<T> {
    public T source;
    public String propertyName;
    public Object newValue;

    public PropertyChangedEventArgs(T source, String propertyName, Object newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.newValue = newValue;
    }
}

interface Observer<T> {
    void handle(PropertyChangedEventArgs<T> args);
}

class Observable<T> {
    private List<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> observer) {
        observers.add(observer);
    }
    
    protected void propertyChanged(T source, String propertyName, Object newValue) {
        for (Observer<T> o : observers) {
            o.handle(new PropertyChangedEventArgs<T>(source, propertyName, newValue));
        }
    }
}

class Person extends Observable<Person> {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (this.age == age) return;
        this.age = age;
        propertyChanged(this, "age", age);
    }
}

public class BasicObserver implements Observer<Person> {
    private static final Logger logger = LoggerFactory.getLogger(BasicObserver.class);

    public static void main(String[] args) {
        new BasicObserver();
    }

    public BasicObserver() {
        Person person = new Person();
        person.subscribe(this);
        for (int i = 20; i < 24; i++) {
            person.setAge(i);
        }
    }

    @Override
    public void handle(PropertyChangedEventArgs<Person> args) {
        logger.info("Person´s {} has changed to {}", args.propertyName, args.newValue);
    }

}
