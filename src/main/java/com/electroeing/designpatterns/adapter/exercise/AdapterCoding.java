package com.electroeing.designpatterns.adapter.exercise;

class Square
{
    public int side;

    public Square(int side)
    {
        this.side = side;
    }
}

interface Rectangle
{
    int getWidth();
    int getHeight();

    default int getArea()
    {
        return getWidth() * getHeight();
    }
}

class SquareToRectangleAdapter implements Rectangle
{
    int width, height;
    public SquareToRectangleAdapter(Square square)
    {
        width = square.side;
        height = square.side;

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}

public class AdapterCoding {
}
