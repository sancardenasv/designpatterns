package com.electroeing.designpatterns.factories.exercise;

import java.util.ArrayList;
import java.util.List;

class Person
{
    public int id;
    public String name;

    public Person(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
}

class PersonFactory
{
    private List<Person> personList = new ArrayList<>();

    public Person createPerson(String name)
    {
        Person newPerson = new Person(personList.size(), name);
        personList.add(newPerson);
        return newPerson;
    }
}