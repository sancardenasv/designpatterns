package com.electroeing.designpatterns.prototype.exercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this(p.x, p.y);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Point{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}

class Line {
    public Point start, end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Line(Line l) {
        this(new Point(l.start), new Point(l.end));
    }

    public Line deepCopy() {
        return new Line(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Line{");
        sb.append("start=").append(start);
        sb.append(", end=").append(end);
        sb.append('}');
        return sb.toString();
    }
}


public class DeepCopyExercise {
    private static final Logger logger = LoggerFactory.getLogger(DeepCopyExercise.class);

    public static void main(String[] args) {
        Line l1 = new Line(new Point(10, 10), new Point(20, 20));
        Line l2 = l1.deepCopy();
        l2.end.x = 100;

        logger.info(l1.toString());
        logger.info(l2.toString());
    }

}
