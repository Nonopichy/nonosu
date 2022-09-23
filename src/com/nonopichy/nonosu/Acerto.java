package com.nonopichy.nonosu;

import java.awt.*;

public class Acerto {

    public int x;
    public int y;
    public int size;
    public float opacity;
    public Image image;
    private int fps;

    public Acerto(Image image, int x, int y, int size, float opacity) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.size = size;
        this.opacity = opacity;
    }

    public void update() {
        if (opacity > 0.1f) {
            fps++;
            if (fps > 5) {
                opacity = opacity - 0.1f;
                fps = 0;
            }
        }
    }

}
