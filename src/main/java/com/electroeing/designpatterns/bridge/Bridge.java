package com.electroeing.designpatterns.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

interface Renderer {
    void renderCircle(float radius);
}

class VectorRenderer implements Renderer {
    private static final Logger logger = LoggerFactory.getLogger(VectorRenderer.class);

    @Override
    public void renderCircle(float radius) {
        logger.info("Drawing a circle of radius {}", radius);
    }
}

class RasterRenderer implements Renderer {
    private static final Logger logger = LoggerFactory.getLogger(RasterRenderer.class);

    @Override
    public void renderCircle(float radius) {
        logger.info("Drawing pixels for a circle of radius {}", radius);
    }
}

abstract class Shape {
    protected  Renderer renderer;

    public Shape(Renderer renderer) {
        this.renderer = renderer;
    }

    public abstract void draw();
    public abstract void resize(float factor);
}

class Circle extends Shape {
    public float radius;

    public Circle(Renderer renderer) {
        super(renderer);
    }

    public Circle(Renderer renderer, float radius) {
        super(renderer);
        this.radius = radius;
    }

    @Override
    public void draw() {
        renderer.renderCircle(radius);
    }

    @Override
    public void resize(float factor) {
        radius *= factor;
    }
}

public class Bridge {
    public static void main(String[] args) {
        RasterRenderer rasterRenderer = new RasterRenderer();
        VectorRenderer vectorRenderer = new VectorRenderer();

        Circle circle = new Circle(rasterRenderer, 5);
        circle.draw();
        circle.resize(2);
        circle.draw();

    }

}
