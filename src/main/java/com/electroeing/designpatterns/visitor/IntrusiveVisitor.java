package com.electroeing.designpatterns.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Expression {
    abstract void print(StringBuilder sb);
}

class DoubleExpression extends Expression {
    private double value;

    public DoubleExpression(double value) {
        this.value = value;
    }

    @Override
    void print(StringBuilder sb) {
        sb.append(value);
    }
}

class AdditionExpresion extends Expression {
    private Expression left;
    private Expression right;

    public AdditionExpresion(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    void print(StringBuilder sb) {
        sb.append("(");
        left.print(sb);
        sb.append("+");
        right.print(sb);
        sb.append(")");
    }
}

public class IntrusiveVisitor {
    private static final Logger logger = LoggerFactory.getLogger(IntrusiveVisitor.class);

    public static void main(String[] args) {
        final AdditionExpresion e = new AdditionExpresion(
                new DoubleExpression(1),
                new AdditionExpresion(
                        new DoubleExpression(2),
                        new DoubleExpression(3)
                )
        );

        final StringBuilder sb = new StringBuilder();
        e.print(sb);

        logger.info("Expression: {}", sb);
    }
}
