package com.nonopichy.nonosu;

import java.awt.*;

public class RectangleObj extends Rectangle {
    public Color color;
    public double size;
    public int x_center;
    public int y_center;

    public float opacity = 0.4f;

    public RectangleObj(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.color = color;
        this.size = height + 30;
        this.x_center = x - (height / 2);
        this.y_center = y - (height / 2);
    }

    public void update(int n, boolean x) {
        double j = size / n;//60
        size = size - j;
        if (x == true) {
            if (opacity < 0.9f) {
                opacity = opacity + 0.1f;
            }
        }
    }
    /*

            rotation+=8;
        if(rotation>=360)

    public void update_color(){
        if (color.getAlpha() < 255)
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() + 1);
    }

     */
}
