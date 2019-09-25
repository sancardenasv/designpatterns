package com.electroeing.designpatterns.prototype;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

class Bar implements Serializable {
    public int moreStuff;
    public int anotherStuff;

    public Bar(int moreStuff, int anotherStuff) {
        this.moreStuff = moreStuff;
        this.anotherStuff = anotherStuff;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Bar{");
        sb.append("moreStuff=").append(moreStuff);
        sb.append(", anotherStuff=").append(anotherStuff);
        sb.append('}');
        return sb.toString();
    }
}

class Foo implements Serializable {
    public int stuff;
    public String whatever;
    public Bar bar;

    public Foo(int stuff, String whatever, Bar bar) {
        this.stuff = stuff;
        this.whatever = whatever;
        this.bar = bar;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Foo{");
        sb.append("stuff=").append(stuff);
        sb.append(", whatever='").append(whatever).append('\'');
        sb.append(", bar=").append(bar);
        sb.append('}');
        return sb.toString();
    }
}

public class CopySerialization {
    private static final Logger logger = LoggerFactory.getLogger(CopySerialization.class);

    public static void main(String[] args) {
        Foo foo = new Foo(42, "life", new Bar(99, 66));
        Foo foo2 = SerializationUtils.roundtrip(foo);

        foo2.whatever = "xyz";
        foo2.bar.anotherStuff = 77;

        logger.info("First obj: {}", foo);
        logger.info("Second obj: {}", foo2);
    }
}
