package com.electroeing.designpatterns.bridge.exercise;

interface Renderer {
    String whatToRenderAs(Shape shape);
}

abstract class Shape
{
    public Renderer renderer;
    public Shape(Renderer renderer){
        this.renderer = renderer;
    }
    public abstract String getName();
}

class Triangle extends Shape
{
    public Triangle(Renderer renderer) {
        super(renderer);
    }

    @Override
    public String getName()
    {
        return "Triangle";
    }

    @Override
    public String toString() {
        return renderer.whatToRenderAs(this);
    }
}

class Square extends Shape
{
    public Square(Renderer renderer) {
        super(renderer);
    }

    @Override
    public String getName()
    {
        return "Square";
    }

    @Override
    public String toString() {
        return renderer.whatToRenderAs(this);
    }
}

class RasterRenderer implements Renderer {

    @Override
    public String whatToRenderAs(Shape shape) {
        return String.format("Drawing %s as pixels", shape.getName());
    }
}
class VectorRenderer implements Renderer {

    @Override
    public String whatToRenderAs(Shape shape) {
        return String.format("Drawing %s as lines", shape.getName());
    }
}

public class BridgeCoding {
    public static void main(String[] args) {
        System.out.println(new Triangle(new RasterRenderer()).toString());
    }
}
