package com.electroeing.designpatterns.proxy.exercise;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Person
{
    private int age;

    public Person(int age)
    {
        this.age = age;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String drink() { return "drinking"; }
    public String drive() { return "driving"; }
    public String drinkAndDrive() { return "driving while drunk"; }
}

class ResponsiblePerson extends Person
{

    public ResponsiblePerson(Person person)
    {
        super(person.getAge());
    }

    @Override
    public String drink() {
        return super.getAge() >= 18 ? super.drink() : "too young";
    }

    @Override
    public String drive() {
        return super.getAge() >= 16 ? super.drive() : "too young";
    }

    @Override
    public String drinkAndDrive() {
        return "dead";
    }

}

public class ProxyCodingExercise {
    private static final Logger logger = LoggerFactory.getLogger(ProxyCodingExercise.class);

    @Test
    public void test()
    {
        Person p = new Person(10);
        ResponsiblePerson rp = new ResponsiblePerson(p);

        assertEquals("too young", rp.drive());
        assertEquals("too young", rp.drink());
        assertEquals("dead", rp.drinkAndDrive());

        rp.setAge(20);

        assertEquals("driving", rp.drive());
        assertEquals("drinking", rp.drink());
        assertEquals("dead", rp.drinkAndDrive());
    }

    public static void main(String[] args) {
        Person p1 = new ResponsiblePerson(new Person(18));
        logger.info("Person with age {} calling dink: {}", p1.getAge(), p1.drink());
        logger.info("Person with age {} calling drive: {}", p1.getAge(), p1.drive());

        Person p2 = new ResponsiblePerson(new Person(16));
        logger.info("Person with age {} calling dink: {}", p2.getAge(), p2.drink());
        logger.info("Person with age {} calling drive: {}", p2.getAge(), p2.drive());

        Person p3 = new ResponsiblePerson(new Person(31));
        logger.info("Person with age {} calling dink: {}", p3.getAge(), p3.drink());
        logger.info("Person with age {} calling drive: {}", p3.getAge(), p3.drive());
        logger.info("Person with age {} calling drink and drive: {}", p3.getAge(), p3.drinkAndDrive());
    }
}
