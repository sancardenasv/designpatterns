package com.electroeing.designpatterns.facade;

import java.util.ArrayList;
import java.util.List;

class Buffer {
    private char[] characters;
    private int lineWith;

    public Buffer(int lineHeight, int lineWith) {
        this.lineWith = lineWith;
        this.characters = new char[lineHeight*lineHeight];
    }

    public char charAt(int x, int y) {
        return characters[y*lineWith+x];
    }
}

class Viewport {
    private final Buffer buffer;
    private final int with;
    private final int height;
    private final int offsetX;
    private final int offsetY;

    public Viewport(Buffer buffer, int with, int height, int offsetX, int offsetY) {
        this.buffer = buffer;
        this.with = with;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public char charAt(int x, int y) {
        return buffer.charAt(x + offsetX, y + offsetY);
    }
}

class Console {
    private List<Viewport> viewports = new ArrayList<>();
    int width, height;

    public Console(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public void addViewport(Viewport viewport){
        viewports.add(viewport);
    }

    // Facade
    public static Console newConsole(int width, int height) {
        Buffer buffer = new Buffer(width, height);
        Viewport viewport = new Viewport(buffer, width, height, 0, 0);
        Console console = new Console(width, height);
        console.addViewport(viewport);
        return console;
    }
    
    public void render() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                for (Viewport vp :
                        viewports) {
                    System.out.println(vp.charAt(x, y));
                }
            }
            System.out.println();
        }
    }
}

public class FacadePattern {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(30, 20);
        Viewport viewport = new Viewport(buffer, 30, 20, 0, 0);
        Console console = new Console(30, 20);
        console.addViewport(viewport);
        console.render();

        Console console1 = Console.newConsole(30, 20);
        console1.render();
    }
}
