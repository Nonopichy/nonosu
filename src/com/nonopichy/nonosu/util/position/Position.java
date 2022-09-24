package com.nonopichy.nonosu.util.position;

import com.nonopichy.nonosu.Main;

public class Position {

    public static int resizeHeight(int y) {
        return Math.round(Main.game.getHeight() * (y / (float) Main.ALTURA));
    }


    public static int resizeWidth(int x) {
        return Math.round(Main.game.getWidth() * (x / (float) Main.LARGURA));
    }

}