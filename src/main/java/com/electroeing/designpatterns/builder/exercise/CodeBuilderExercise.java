package com.electroeing.designpatterns.builder.exercise;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class Field {
    protected String name, type;
}

class CodeBuilder {
    private final String newLine = System.lineSeparator();

    private String className;
    private List<Field> fieldList = new ArrayList<>();

    public CodeBuilder(String className){
        this.className = className;
    }

    public CodeBuilder addField(String name, String type) {
        Field field = new Field();
        field.name = name;
        field.type = type;
        fieldList.add(field);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("public class ")
                .append(this.className)
                .append(newLine)
                .append("{");
        for (Field field: fieldList) {
            builder.append(newLine)
                    .append("  public ")
                    .append(field.type)
                    .append(" ")
                    .append(field.name)
                    .append(";");
        }

        builder.append(newLine)
                .append("}");

        return builder.toString();
    }
}

public class CodeBuilderExercise {
    public static void main(String[] args) {
        CodeBuilder cb = new CodeBuilder("Person")
                .addField("name", "String")
                .addField("age", "int");

        System.out.println(cb);
    }
}

class Evaluate
{
    private String preprocess(String text)
    {
        return text.replace("\r\n", "\n").trim();
    }

    @Test
    public void emptyTest() {
        CodeBuilder cb = new CodeBuilder("Foo");
        Assertions.assertEquals("public class Foo\n{\n}",
                preprocess(cb.toString()));
    }

    @Test
    public void personTest()
    {
        CodeBuilder cb = new CodeBuilder("Person")
                .addField("name", "String")
                .addField("age", "int");
        Assertions.assertEquals("public class Person\n{\n" +
                        "  public String name;\n" +
                        "  public int age;\n}",
                preprocess(cb.toString()));
    }
}
