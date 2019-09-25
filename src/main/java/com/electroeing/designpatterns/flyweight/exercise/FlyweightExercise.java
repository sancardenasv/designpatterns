package com.electroeing.designpatterns.flyweight.exercise;

import java.util.ArrayList;
import java.util.List;

class Sentence {
    private String plainText;
    private List<WordToken> wordTokens = new ArrayList<>();

    public Sentence(String plainText) {
        this.plainText = plainText;
    }

    public WordToken getWord(int index) {
        WordToken token = new WordToken(index);
        wordTokens.add(token);
        return token;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] strings = plainText.split(" ");
        for (int i = 0; i < strings.length; i++) {
            for (WordToken token : wordTokens) {
                if (token.applies(i) && token.capitalize) {
                    sb.append(strings[i].toUpperCase());
                } else {
                    sb.append(strings[i]);
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    class WordToken {
        public int index;
        public boolean capitalize;

        public WordToken(int index) {
            this.index = index;
        }

        public boolean applies(int index) {
            return this.index == index;
        }
    }
}

public class FlyweightExercise {
    public static void main(String[] args) {
        Sentence sentence = new Sentence("Hello World");
        sentence.getWord(1).capitalize = true;
        System.out.println(sentence.toString());
    }
}
