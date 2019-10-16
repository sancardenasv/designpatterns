package com.electroeing.designpatterns.strategy;

import com.electroeing.designpatterns.proxy.DynamicProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

enum OutputFormat {
    MARKDOWN,
    HTML
}

interface ListStrategy {
    default void start(StringBuilder sb){}
    void addListItem(StringBuilder sb, String item);
    default void end(StringBuilder sb){}
}

class MarkdownListStrategy implements ListStrategy {
    @Override
    public void addListItem(StringBuilder sb, String item) {
        sb.append(" * ").append(item)
                .append(System.lineSeparator());
    }
}

class HrmlStrategy implements ListStrategy {
    @Override
    public void start(StringBuilder sb) {
        sb.append("<ul>").append(System.lineSeparator());
    }

    @Override
    public void addListItem(StringBuilder sb, String item) {
        sb.append("   <li>").append(item).append("</li>").append(System.lineSeparator());
    }

    @Override
    public void end(StringBuilder sb) {
        sb.append("</ul>").append(System.lineSeparator());
    }
}

class TestProcessor {
    private StringBuilder sb = new StringBuilder();
    private ListStrategy listStrategy;

    public TestProcessor(OutputFormat format) {
        setOutputFormat(format);
    }

    public void setOutputFormat(OutputFormat format) {
        switch (format) {
            case MARKDOWN:
                listStrategy = new MarkdownListStrategy();
                break;
            case HTML:
                listStrategy = new HrmlStrategy();
                break;
        }
    }

    public void appendList(List<String> items) {
        listStrategy.start(sb);
        for (String item :
                items) {
            listStrategy.addListItem(sb, item);
        }
        listStrategy.end(sb);
    }

    public void clear() {
        sb.setLength(0);
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}

public class DynamicStrategy {
    private final static Logger logger = LoggerFactory.getLogger(DynamicProxy.class);

    public static void main(String[] args) {
        final TestProcessor tp = new TestProcessor(OutputFormat.MARKDOWN);
        tp.appendList(Arrays.asList("liberte", "egalite", "fraternite"));
        logger.info("{}", tp);

        tp.clear();
        tp.setOutputFormat(OutputFormat.HTML);
        tp.appendList(Arrays.asList("inheritance", "encapsulation", "polymorphism"));
        logger.info("{}", tp);
    }
}
