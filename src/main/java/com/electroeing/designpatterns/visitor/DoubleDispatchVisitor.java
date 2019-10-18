package com.electroeing.designpatterns.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

interface ExpressionVisitor {
    void visit(DoubleExpression3 e);
    void visit(AdditionExpresion3 e);
    void visit(SubtractionExpression e);
}

abstract class Expression3 {
    public abstract void accept(ExpressionVisitor visitor);
}

class DoubleExpression3 extends Expression3 {
    private double value;

    public DoubleExpression3(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}

class AdditionExpresion3 extends Expression3 {
    private Expression3 left;
    private Expression3 right;

    public AdditionExpresion3(Expression3 left, Expression3 right) {
        this.left = left;
        this.right = right;
    }

    public Expression3 getLeft() {
        return left;
    }

    public Expression3 getRight() {
        return right;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}

class SubtractionExpression extends Expression3 {
    private Expression3 left;
    private Expression3 right;

    public SubtractionExpression(Expression3 left, Expression3 right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    public Expression3 getLeft() {
        return left;
    }

    public Expression3 getRight() {
        return right;
    }
}

class ExpressionPrinter2 implements ExpressionVisitor {
    private StringBuilder sb = new StringBuilder();

    @Override
    public void visit(DoubleExpression3 e) {
        sb.append(e.getValue());
    }

    @Override
    public void visit(AdditionExpresion3 e) {
        sb.append("(");
        e.getLeft().accept(this);
        sb.append("+");
        e.getRight().accept(this);
        sb.append(")");
    }

    @Override
    public void visit(SubtractionExpression e) {
        sb.append("(");
        e.getLeft().accept(this);
        sb.append("-");
        e.getRight().accept(this);
        sb.append(")");
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}

class ExpressionCalculator implements ExpressionVisitor {
    private double result;

    @Override
    public void visit(DoubleExpression3 e) {
        result = e.getValue();
    }

    @Override
    public void visit(AdditionExpresion3 e) {
        e.getLeft().accept(this);
        double a = result;
        e.getRight().accept(this);
        double b = result;

        result = a + b;
    }

    @Override
    public void visit(SubtractionExpression e) {
        e.getLeft().accept(this);
        double a = result;
        e.getRight().accept(this);
        double b = result;

        result = a - b;
    }

    public double getResult() {
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(result);
    }
}

public class DoubleDispatchVisitor {
    private static final Logger logger = LoggerFactory.getLogger(DoubleDispatchVisitor.class);

    public static void main(String[] args) {
        // 1 + (2 + (4 - 1)))
        final AdditionExpresion3 e = new AdditionExpresion3(
                new DoubleExpression3(1),
                new AdditionExpresion3(
                        new DoubleExpression3(2),
                        new SubtractionExpression(
                                new DoubleExpression3(4),
                                new DoubleExpression3(1))
                )
        );

        final ExpressionPrinter2 ep = new ExpressionPrinter2();
        ep.visit(e);
        logger.info("Expression: {}", ep);

        final ExpressionCalculator ec = new ExpressionCalculator();
        ec.visit(e);
        logger.info("With result: {}={}", ep, ec);
    }
}
