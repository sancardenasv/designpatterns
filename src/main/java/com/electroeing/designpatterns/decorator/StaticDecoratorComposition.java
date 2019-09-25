package com.electroeing.designpatterns.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

class ColoredShapeStatic<T extends Shape> implements Shape {
    private Shape shape;
    private String color;

    public ColoredShapeStatic(Supplier<? extends T> ctor, String color) {
        shape = ctor.get();
        this.color = color;
    }

    @Override
    public String info() {
        return shape.info() + " has the color " + color;
    }
}

class TransparentShapeStatic<T extends Shape> implements Shape {
    private Shape shape;
    private int transparency;

    public TransparentShapeStatic(Supplier<? extends T> ctor, int transperency) {
        this.shape = ctor.get();
        this.transparency = transperency;
    }

    @Override
    public String info() {
        return shape.info() + " has transparency " + transparency + "%";
    }
}

public class StaticDecoratorComposition {
    private static final Logger logger = LoggerFactory.getLogger(StaticDecoratorComposition.class);

    public static void main(String[] args) {
        ColoredShapeStatic<Square> blueSquare = new ColoredShapeStatic<>(() -> new Square(20), "blue");
        logger.info(blueSquare.info());

        TransparentShapeStatic<ColoredShapeStatic<Circle>> myCircle =
                new TransparentShapeStatic<>(() -> new ColoredShapeStatic<>(() -> new Circle(5), "green"), 5);
        logger.info(myCircle.info());
    }
}
