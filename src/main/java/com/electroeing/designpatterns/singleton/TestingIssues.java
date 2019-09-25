package com.electroeing.designpatterns.singleton;

import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

interface Database {
    int getPopulation(String name);
}

class SingletonDatabase implements Database {
    private static final Logger logger = LoggerFactory.getLogger(SingletonDatabase.class);

    private static final SingletonDatabase INSTANCE = new SingletonDatabase();
    private Dictionary<String, Integer> cities = new Hashtable<>();
    private static int instanceCount = 0;

    private SingletonDatabase() {
        instanceCount++;
        logger.info("Initializing database");
        try {
            File file = new File(SingletonDatabase.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            Path path = Paths.get(file.getPath(), "cities.txt");
            List<String> lines = Files.readAllLines(path);
            Iterables.partition(lines, 2)
                    .forEach(kv -> cities.put(kv.get(0), Integer.parseInt(kv.get(1))));
        } catch (IOException e) {
            logger.error("Error loading", e);
        }
    }

    public static SingletonDatabase getInstance() {
        return INSTANCE;
    }

    public int getPopulation(String city) {
        return cities.get(city);
    }
}

class SingletonRecordFinder {
    public int getTotalPopulation(List<String> names) {
        int result = 0;
        for (String name: names) {
            result += SingletonDatabase.getInstance().getPopulation(name);
        }

        return result;
    }
}

class ConfigurableRecordFinder {
    private Database database;

    public ConfigurableRecordFinder(Database database) {
        this.database = database;
    }

    public int getTotalPopulation(List<String> names) {
        int result = 0;
        for (String name: names) {
            result += database.getPopulation(name);
        }

        return result;
    }
}

class DummyDatabase implements Database {
    private Dictionary<String, Integer> data = new Hashtable<>();

    public DummyDatabase() {
        data.put("alpha", 10);
        data.put("beta", 6);
        data.put("gamma", 8);
    }

    @Override
    public int getPopulation(String name) {
        return data.get(name);
    }
}

class Tests {
    // This is an integration test not unit
    @Test
    public void singletonTotalPopulationTest() {
        SingletonRecordFinder rf = new SingletonRecordFinder();
        List<String> names = Arrays.asList("Seoul", "Mexico City");

        int tp = rf.getTotalPopulation(names);
        assertThat(tp).isEqualTo(17500000 + 17400000);
    }

    // This is a unit test
    @Test
    public void dependentPopulationTest(){
        DummyDatabase dummyDatabase = new DummyDatabase();
        ConfigurableRecordFinder rf = new ConfigurableRecordFinder(dummyDatabase);

        assertThat(rf.getTotalPopulation(Arrays.asList("alpha", "gamma"))).isEqualTo(18);
    }
}

public class TestingIssues {
}
