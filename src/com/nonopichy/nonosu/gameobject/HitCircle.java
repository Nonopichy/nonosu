package com.nonopichy.nonosu.gameobject;

public class HitCircle extends Circle {
    public int life;

    public HitCircle(int pos, int x, int y, int size, int life) {
        super(pos, x, y, size);
        this.life = life;
    }
}
