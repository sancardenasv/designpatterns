package com.electroeing.designpatterns.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Expression2 {

}

class DoubleExpression2 extends Expression2 {
    private double value;

    public DoubleExpression2(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}

class AdditionExpresion2 extends Expression2 {
    private Expression2 left;
    private Expression2 right;

    public AdditionExpresion2(Expression2 left, Expression2 right) {
        this.left = left;
        this.right = right;
    }

    public Expression2 getLeft() {
        return left;
    }

    public Expression2 getRight() {
        return right;
    }
}

class ExpressionPrinter {
    public static void print(Expression2 e, StringBuilder sb) {
        if (e.getClass().equals(DoubleExpression2.class)) {
            sb.append(((DoubleExpression2) e).getValue());
        } else if (e.getClass().equals(AdditionExpresion2.class)) {
            AdditionExpresion2 ae = (AdditionExpresion2) e;
            sb.append("(");
            print(ae.getLeft(), sb);
            sb.append("+");
            print(ae.getRight(), sb);
            sb.append(")");
        }
    }
}

public class ReflectiveVisitor {
    private static final Logger logger = LoggerFactory.getLogger(ReflectiveVisitor.class);

    public static void main(String[] args) {
        final AdditionExpresion2 e = new AdditionExpresion2(
                new DoubleExpression2(1),
                new AdditionExpresion2(
                        new DoubleExpression2(2),
                        new DoubleExpression2(3)
                )
        );

        final StringBuilder sb = new StringBuilder();
        ExpressionPrinter.print(e, sb);

        logger.info("Expression: {}", sb);
    }
}
