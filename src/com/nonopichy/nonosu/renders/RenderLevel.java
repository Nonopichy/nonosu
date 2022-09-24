package com.nonopichy.nonosu.renders;

import com.nonopichy.nonosu.Main;
import com.nonopichy.nonosu.level.RemakeLevel;
import com.nonopichy.nonosu.util.position.Position;

import java.awt.*;

public class RenderLevel {

    public int largura;
    public int altura;
    public RemakeLevel level;

    public Image background;
    public Color Opacity;

    public RenderLevel(int largura, int altura, RemakeLevel level, int opacity) {
        this.largura = largura;
        this.altura = altura;
        this.level = level;

        this.Opacity = new Color(0, 0, 0, opacity);

        refresh();
    }

    public void render(Graphics g) {
        if (!validLevel())
            return;
        if (background == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Main.game.getWidth(), Main.game.getHeight());
        } else {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(background, 0, 0, Main.game.getWidth(), Main.game.getHeight(), Main.game);
            g2.setColor(Opacity);
            g2.fillRect(0, 0, Main.game.getWidth(), Main.game.getHeight());
        }

        g.setColor(Color.GREEN);
        //g.fillRect(10, 10, Main.life, 10);
        fillRect(g, 10, 10, Main.life, 10);

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.ITALIC, 16));
        //g.drawString("Points: " + Main.pontuacao, 5, 470);
        drawString(g, "Points: " + Main.pontuacao, 5, 470);

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.ITALIC, 16));
        //g.drawString("Combo: " + Main.combo, 5, 455);
        drawString(g, "Combo: " + Main.combo, 5, 455);

        //g.setColor(Color.RED);
        //g.setFont(new Font("Arial", Font.ITALIC, 16));
        //   g.drawString("[DeBug] Now: " + level.now + ", Timer:" + level.timer + ", N: " + level.n
        //         + "Rec.size: " + level.rec.size() + ", Preview.size: " +level.preview.size() + ", RealFrame: " + level.real_frames[0], 5, 30);

        level.render(g);
    }

    public void fillRect(Graphics g, int x, int y, int width, int height) {
        g.fillRect(Position.resizeWidth(x), Position.resizeHeight(y), width, height);
    }

    public void drawString(Graphics g, String str, int x, int y) {
        g.drawString(str, Position.resizeWidth(x), Position.resizeHeight(y));
    }

    public void refresh() {
        if (!validLevel())
            return;
        this.background = level.background;
    }

    public Boolean validLevel() {
        if (level == null)
            return false;
        return level.ready == true;
    }

    public void update() {
        if (!validLevel())
            return;
        level.update();

        if (Main.life < 1)
            Main.life = 200;
        else if (Main.life > 200)
            Main.life = 200;


    }

    public void setLevel(RemakeLevel level) {
        this.level = level;
        refresh();
    }

    public void setOpacity(int opacity) {
        this.Opacity = new Color(0, 0, 0, opacity);
    }
}
