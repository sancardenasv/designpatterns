package com.electroeing.designpatterns.mediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class Person {
    private static final Logger logger = LoggerFactory.getLogger(Person.class);

    public String name;
    private List<String> chatlog = new ArrayList<>();
    public Room room;

    public Person(String name) {
        this.name = name;
    }

    public void receive(String sender, String message) {
        String s = sender + ": '" + message + "'";
        logger.info("[{}'s chat session] {}", name, s);
        chatlog.add(s);
    }

    public void say(String message) {
        room.broadcast(name, message);
    }

    public void privateMessage(String who, String message) {
        room.message(name, who, message);
    }
}

class Room {
    private List<Person> people = new ArrayList<>();

    public void join(Person p) {
        String join = p.name + " joining the room";
        broadcast("room", join);

        p.room = this;
        people.add(p);
    }

    public void broadcast(String source, String message) {
        for (Person p: people) {
            if (!p.name.equals(source)) {
                p.receive(source, message);
            }
        }
    }

    public void message(String source, String dest, String message) {
        people.stream()
                .filter(p -> p.name.equals(dest))
                .findFirst()
                .ifPresent(person -> person.receive(source, message));
    }

}

public class ChatRoom {
    private static final Logger logger = LoggerFactory.getLogger(ChatRoom.class);

    public static void main(String[] args) {
        final Room room = new Room();
        final Person john = new Person("John");
        final Person jane = new Person("Jane");
        room.join(john);
        room.join(jane);

        john.say("hi room");
        jane.say("oh, hey john");

        final Person simon = new Person("Simon");
        room.join(simon);
        simon.say("Hello everyone!");

        jane.privateMessage("Simon", "glad you could join us!");
    }
}
