package com.electroeing.designpatterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class Event<TArgs> {
    private int count = 0;
    private Map<Integer, Consumer<TArgs>> handlers = new HashMap<>();

    public Subscription addHandler(Consumer<TArgs> handler) {
        int i = count;
        handlers.put(count++, handler);
        return new Subscription(this, i);
    }

    public void fire(TArgs args) {
        for (Consumer<TArgs> handler : handlers.values()) {
            handler.accept(args);
        }
    }

    public class Subscription implements AutoCloseable {
        private Event<TArgs> event;
        private int id;

        public Subscription(Event<TArgs> event, int id) {
            this.event = event;
            this.id = id;
        }

        @Override
        public void close() {
            event.handlers.remove(id);
        }
    }
}

class PropertyChangedEventArgs2 {
    public Object source;
    public String propertyName;

    public PropertyChangedEventArgs2(Object source, String propertyName) {
        this.source = source;
        this.propertyName = propertyName;
    }
}

class Person2 {
    public Event<PropertyChangedEventArgs2> propertyChanged = new Event<>();
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (this.age == age) return;
        this.age = age;
        propertyChanged.fire(new PropertyChangedEventArgs2(this, "age"));
    }
}

public class EventClass {
    private static final Logger logger = LoggerFactory.getLogger(EventClass.class);

    public static void main(String[] args) {
        final Person2 person2 = new Person2();
        final Event<PropertyChangedEventArgs2>.Subscription subscription = person2.propertyChanged.addHandler(x -> {
            logger.info("Person's {} has changed", x.propertyName);
        });
        final Person2 person3 = new Person2();
        final Event<PropertyChangedEventArgs2>.Subscription subscription2 = person3.propertyChanged.addHandler(x -> {
            logger.info("3rd Person's {} has changed", x.propertyName);
        });

        person2.setAge(17);
        person2.setAge(18);
        person3.setAge(18);
        subscription.close();
        person2.setAge(19);
        subscription2.close();
    }
}
