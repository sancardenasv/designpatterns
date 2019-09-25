package com.electroeing.designpatterns.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Dangerous approach, as people can think this is not a singleton and create multiple instances of the class
without noticing the storage is shared.
 */
class ChiefExecutiveOfficer {
    private static String name;
    private static int age;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChiefExecutiveOfficer{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        ChiefExecutiveOfficer.name = name;
    }

    public static int getAge() {
        return age;
    }

    public void setAge(int age) {
        ChiefExecutiveOfficer.age = age;
    }
}
public class MonoStateSingleton {
    private static final Logger logger = LoggerFactory.getLogger(MonoStateSingleton.class);

    public static void main(String[] args) {
        ChiefExecutiveOfficer ceo = new ChiefExecutiveOfficer();
        ceo.setName("Pablo Fuentes");
        ceo.setAge(42);

        ChiefExecutiveOfficer ceo2 = new ChiefExecutiveOfficer();
        logger.info("Last ceo: {}", ceo2);

    }
}
