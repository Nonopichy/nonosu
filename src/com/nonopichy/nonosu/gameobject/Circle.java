package com.nonopichy.nonosu.gameobject;

public abstract class Circle {

    public int pos;
    public int x;
    public int y;
    public int size;

    public Circle(int pos, int x, int y, int size) {
        this.pos = pos;
        this.x = x;
        this.y = y;
        this.size = size;
    }

}
