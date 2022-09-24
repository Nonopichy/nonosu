package com.nonopichy.nonosu.maker;

public class Frame {
    public int x;
    public int y;
    public int timer;

    public Frame(int x, int y, int timer) {
        this.x = x;
        this.y = y;
        this.timer = timer;
    }

    public static Frame fromString(String str) {
        String[] list = str.split("~");
        //2 == y
        //0 == timer
        //1 == x
        return new Frame(Integer.parseInt(list[1]), Integer.parseInt(list[2]), Integer.parseInt(list[0]));
    }

    public String toString() {
        return timer + "~" + x + "~" + y;
    }
}
