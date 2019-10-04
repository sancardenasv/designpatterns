package com.electroeing.designpatterns.interpreter.exercise;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

interface Element {
    int eval();
}

class Number implements Element {
    private int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public int eval() {
        return this.value;
    }
}

class Variable implements Element {
    private String name;
    private Map<Character, Integer> variables;

    public Variable(String name, Map<Character, Integer> variables) {
        this.name = name;
        this.variables = variables;
    }

    @Override
    public int eval() {
        int value = 0;
        if (name.length() == 1 && variables.containsKey(name.charAt(0))) {
            value = variables.get(name.charAt(0));
        }
        return value;
    }
}

class BinaryOperation implements Element {
    public enum Type {
        ADDITION,
        SUBTRACTION
    }

    public Type type;
    public Element left, right;

    public BinaryOperation() {

    }

    public BinaryOperation(BinaryOperation operation) {
        this.left = operation.left;
        this.right = operation.right;
        this.type = operation.type;
    }


    @Override
    public int eval() {
        if (type == null) {
            return left == null ? 0 : left.eval();
        }
        switch (type) {
            case ADDITION:
                return left.eval() + right.eval();
            case SUBTRACTION:
                return left.eval() - right.eval();
            default:
                return 0;
        }
    }
}

class Token {
    public enum  Type {
        NUMBER,
        PLUS,
        MINUS,
        VARIABLE
    }

    public Type type;
    public String text;

    public Token(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        return "`".concat(text).concat("`");
    }
}

class ExpressionProcessor {
    private final static Logger logger = LoggerFactory.getLogger(ExpressionProcessor.class);

    public Map<Character, Integer> variables = new HashMap<>();

    private List<Token> lex(String input) {
        ArrayList<Token> result = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '+':
                    result.add(new Token(Token.Type.PLUS, "+"));
                    break;
                case '-':
                    result.add(new Token(Token.Type.MINUS, "-"));
                    break;
                default:
                    final boolean isNumber = Character.isDigit(input.charAt(i));
                    final StringBuilder sb = new StringBuilder(String.valueOf(input.charAt(i)));
                    for (int j = i + 1; j < input.length() ; j++) {
                        if ((isNumber && Character.isDigit(input.charAt(j))) || (!isNumber && Character.isAlphabetic(input.charAt(j)))) {
                            sb.append(input.charAt(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                    result.add(new Token(isNumber ? Token.Type.NUMBER : Token.Type.VARIABLE, sb.toString()));
                    break;
            }
        }
        return result;
    }

    private Element parse(List<Token> tokens) {
        final BinaryOperation result = new BinaryOperation();
        boolean haveLHS = false;

        for (int i = 0; i < tokens.size(); i++) {
            if (haveLHS && result.right != null) {
                result.left = new BinaryOperation(result);
                result.right = null;
                result.type = null;
            }
            final Token token = tokens.get(i);
            switch (token.type) {
                case NUMBER:
                    final Number integer = new Number(java.lang.Integer.parseInt(token.text));
                    if (!haveLHS) {
                        result.left = integer;
                        haveLHS = true;
                    } else {
                        result.right = integer;
                    }
                    break;
                case PLUS:
                    result.type = BinaryOperation.Type.ADDITION;
                    break;
                case MINUS:
                    result.type = BinaryOperation.Type.SUBTRACTION;
                    break;
                case VARIABLE:
                    final Variable var = new Variable(token.text, variables);
                    if (!haveLHS) {
                        result.left = var;
                        haveLHS = true;
                    } else {
                        result.right = var;
                    }
                    break;
            }
        }
        return result;
    }

    public int calculate(String expression) {
        final List<Token> tokens = lex(expression);

        final List<Token> collect1 = tokens.stream().filter(t -> t.type.equals(Token.Type.VARIABLE) && t.text.length() > 1).collect(Collectors.toList());
        if (!collect1.isEmpty()) {
            return 0;
        }
        final String collect = tokens.stream()
                .map(Token::toString)
                .collect(Collectors.joining("|"));
        logger.info(collect);
        return parse(tokens).eval();
    }
}

public class InterpreterCodingExercise {
    private final static Logger logger = LoggerFactory.getLogger(InterpreterCodingExercise.class);

    @Test
    public void simpleTest() {
        String expression = "1+2+3";
        final ExpressionProcessor expressionProcessor = new ExpressionProcessor();
        final int result = expressionProcessor.calculate(expression);
        logger.info("Evaluating {} = {}", expression, result);
        assertEquals(6, result);
    }

    @Test
    public void usingVariablesTest() {
        Map<Character, Integer> variables = new HashMap<>();
        variables.put('x', 6);
        variables.put('y', 2);
        String expression = "y-1-4+8-x";
        final ExpressionProcessor expressionProcessor = new ExpressionProcessor();
        expressionProcessor.variables = variables;
        final int result = expressionProcessor.calculate(expression);
        logger.info("Evaluating {} = {}", expression, result);
        assertEquals(-1, result);
    }

    @Test
    public void variableNotFoundTest() {
        Map<Character, Integer> variables = new HashMap<>();
        variables.put('x', 2);
        String expression = "x+x+x+1-w";
        final ExpressionProcessor expressionProcessor = new ExpressionProcessor();
        expressionProcessor.variables = variables;
        final int result = expressionProcessor.calculate(expression);
        logger.info("Evaluating {} = {}", expression, result);
        assertEquals(7, result);
    }

    @Test
    public void lettersGroupedTest() {
        Map<Character, Integer> variables = new HashMap<>();
        variables.put('x', 5);
        variables.put('y', 10);
        String expression = "y-2-x+xy";
        final ExpressionProcessor expressionProcessor = new ExpressionProcessor();
        expressionProcessor.variables = variables;
        final int result = expressionProcessor.calculate(expression);
        logger.info("Evaluating {} = {}", expression, result);
        assertEquals(0, result);
    }

    @Test
    public void test()
    {
        ExpressionProcessor ep = new ExpressionProcessor();
        ep.variables.put('x', 5);

        assertEquals(1, ep.calculate("1"));
        assertEquals(3, ep.calculate("1+2"));
        assertEquals(6, ep.calculate("1+x"));
        assertEquals(0, ep.calculate("1+xy"));
    }

}
