package com.electroeing.designpatterns.interpreter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface Element {
    int eval();
}

class Integer implements Element {
    private int value;

    public Integer(int value) {
        this.value = value;
    }

    @Override
    public int eval() {
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

    @Override
    public int eval() {
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
        INTEGER,
        PLUS,
        MINUS,
        LPAREN,
        RPAREN
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

public class LexingInterpreter {
    private static final Logger logger = LoggerFactory.getLogger(LexingInterpreter.class);

    static List<Token> lex(String input) {
        ArrayList<Token> result = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '+':
                    result.add(new Token(Token.Type.PLUS, "+"));
                    break;
                case '-':
                    result.add(new Token(Token.Type.MINUS, "-"));
                    break;
                case '(':
                    result.add(new Token(Token.Type.LPAREN, "("));
                    break;
                case ')':
                    result.add(new Token(Token.Type.RPAREN, ")"));
                    break;
                default:
                    final StringBuilder sb = new StringBuilder(String.valueOf(input.charAt(i)));
                    for (int j = i + 1; j <input.length() ; j++) {
                        if (Character.isDigit(input.charAt(j))) {
                            sb.append(input.charAt(j));
                            ++i;
                        } else {
                            result.add(new Token(Token.Type.INTEGER, sb.toString()));
                            break;
                        }
                    }
                    break;
            }
        }
        return result;
    }

    static Element parse(List<Token> tokens) {
        final BinaryOperation result = new BinaryOperation();
        boolean haveLHS = false;

        for (int i = 0; i < tokens.size(); i++) {
            final Token token = tokens.get(i);
            switch (token.type) {
                case INTEGER:
                    final Integer integer = new Integer(java.lang.Integer.parseInt(token.text));
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
                case LPAREN:
                    int j = i; // Get the Rparen position
                    for (; j < tokens.size(); ++j) {
                        if (tokens.get(j).type == Token.Type.RPAREN) {
                            break;
                        }
                    }
                    final List<Token> subExpression = tokens.stream()
                            .skip(i + 1L)
                            .limit(j - i - 1L)
                            .collect(Collectors.toList());
                    final Element element = parse(subExpression);
                    if (!haveLHS) {
                        result.left = element;
                        haveLHS = true;
                    } else {
                        result.right = element;
                    }
                    i = j;
                    break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        final String input = "(13 + 4) - (12 + 1)";
        final List<Token> tokens = lex(input.replace(" ", ""));
        final String string = tokens.stream()
                .map(Token::toString)
                .collect(Collectors.joining("|"));
        logger.info(string);

        Element parsed = parse(tokens);
        logger.info("{} = {}", input, parsed.eval());
    }
}
