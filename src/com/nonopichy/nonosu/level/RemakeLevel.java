package com.nonopichy.nonosu.level;

import com.nonopichy.nonosu.Main;
import com.nonopichy.nonosu.gameobject.Acerto;
import com.nonopichy.nonosu.gameobject.GhostCircle;
import com.nonopichy.nonosu.gameobject.HitCircle;
import com.nonopichy.nonosu.gameobject.RectangleObj;
import com.nonopichy.nonosu.maker.Frame;
import com.nonopichy.nonosu.util.Util;
import com.nonopichy.nonosu.util.position.Position;
import com.nonopichy.nonosu.util.sound.MP3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class RemakeLevel {

    public static final double GLOBAL_SIZE = 1;
    public static final double LIFE = 40 * GLOBAL_SIZE;
    public static final double SIZE = 60 * GLOBAL_SIZE;
    public static final double SIZE_HIT = 60 * GLOBAL_SIZE;
    public static final double SIZE_PREVIEW = 140 * GLOBAL_SIZE;
    public static Color white = new Color(255, 255, 255, 131);
    public int timer = 0;
    public List<RectangleObj> rec = new ArrayList<>();
    public List<GhostCircle> ghostCircles = new ArrayList<>();
    public List<HitCircle> hitCircles = new ArrayList<>();
    public List<Acerto> acerto = new ArrayList<>();
    public String[] frames;
    public int now = 0;
    public String[] real_frames;
    //  public WaveFile tap = new SoundEffect("TAP");
    public MP3 sound;
    public Color c_preview_1 = new Color(176, 224, 230, 127);
    // public Color c_preview_2 = new Color(176,224,230, 110);
    public Color c_clicker = new Color(150, 224, 230, 255);
    public Boolean goal = false;
    public Boolean ready = false;
    public Image background;
    public int n;
    public int pos = 0;

    public RemakeLevel(String level) {
        Main.state = State.GAME;

        try {
            frames = Util.readTxt("levels/" + level + "/notas.txt").split(";");
            sound = new MP3("levels/" + level + "/sound.mp3");
            File file = new File("levels/" + level + "/background.png");
            if (file.exists())
                background = ImageIO.read(new File("levels/" + level + "/background.png"));
            Main.pontuacao = 0;
            Main.combo = 0;
            Main.ruins = 0;
            Main.meidas = 0;
            Main.boas = 0;
            Main.falha = 0;

            ready = true;
            sound.play();
            Main.renderLevel.setLevel(this);


            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // MyUtil.playVideo("levels/"+level+"/background.mp4");
        // Game.jpanel.setVisible(true);

    }

    public static void drawImage(Graphics2D g, Image image, int x, int y, int r, float opacity) {
        x = x - (r / 2);
        x = Position.resizeWidth(x);
        y = y - (r / 2);
        y = Position.resizeHeight(y);
        r = Position.resizeHeight(r);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g.drawImage(image, x, y, r, r, Main.game);
    }

    public void update() {
        timer++;

        if (pos >= frames.length) {
            Main.state = State.END;
            return;
        }

        Frame frame = Frame.fromString(frames[pos]);
        int frameX = frame.x;
        int frameY = frame.y;
        int frameTimer = frame.timer;

        if (timer >= frameTimer) {
            hitCircles.add(new HitCircle(pos, frameX, frameY, (int) SIZE_HIT, (int) LIFE));
            ghostCircles.add(new GhostCircle(pos, frameX, frameY, (int) SIZE_PREVIEW));
            pos++;
        }

        // Lifetime reduce GhostCircle.
        ArrayList<GhostCircle> ghostCirclesRemove = new ArrayList<>();
        ArrayList<HitCircle> hitCirclesRemove = new ArrayList<>();

        for (HitCircle hitCircle : hitCircles) {
            hitCircle.life -= GLOBAL_SIZE;
            if (hitCircle.life <= 0) {
                hitCirclesRemove.add(hitCircle);
            }
        }

        for (GhostCircle ghostCircle : ghostCircles) {
            ghostCircle.size -= Math.max(2, 2 * GLOBAL_SIZE);
            if (ghostCircle.size <= SIZE) {
                ghostCirclesRemove.add(ghostCircle);
            }
        }

        // Remove hit circle, finish!
        for (HitCircle hitCircle : hitCirclesRemove) {
            hitCircles.remove(hitCircle);
        }

        // Remove ghost circle, finish!
        for (GhostCircle ghostCircle : ghostCirclesRemove) {
            ghostCircles.remove(ghostCircle);
        }


        if (timer > frameTimer) {
            //spawn
        }


        goal = false;
        Main.mx = 99999;
        Main.my = 99999;
    }

    public void render(Graphics g) {
        renderHit(g);
        renderPreview(g);

        //renderClicker(g);
        //renderAnimationStrings(g);
        //  renderLine(g);
    }

    public void renderAnimationStrings(Graphics g) {
        for (int i = 0; i < acerto.size(); i++) {
            Acerto c = acerto.get(i);
            Graphics2D g2 = (Graphics2D) g;
            drawImage(g2, c.image, c.x, c.y, 140, c.opacity);
        }
    }

    public void renderPreview(Graphics g) {
        for (int i = 0; i < ghostCircles.size(); i++) {
            GhostCircle c = ghostCircles.get(i);
            Graphics2D g2 = (Graphics2D) g;
            //  g2.rotate(Math.toRadians(c.rotation),c.x+c.width/2,c.y+c.height/2);
            //  g2.setColor(c.color);
            //  drawCenteredOvalCircle(g2,c.x,c.y, (int) c.size);
            //  g2.rotate(Math.toRadians(-c.rotation),c.x+c.width/2,c.y+c.height/2);
            drawImage(g2, Main.preview, c.x, c.y, c.size, 100);
        }
    }

    public void renderHit(Graphics g) {
        for (HitCircle hitCircle : hitCircles) {
            drawImage((Graphics2D) g, Main.hit_background, hitCircle.x, hitCircle.y, hitCircle.size, 0.9f);
            drawImage((Graphics2D) g, Main.hit_overlay, hitCircle.x, hitCircle.y, hitCircle.size, 0.9f);
        }
    }

    /*
        public void renderClicker(Graphics g) {
            for (int i = 0; i < rec.size(); i++) {
                RectangleObj c = rec.get(i);
                Graphics2D g2 = (Graphics2D) g;
                //  g2.rotate(Math.toRadians(c.rotation),c.x+c.width/2,c.y+c.height/2);
                //  g2.setColor(c.color);
                //  drawCenteredOvalCircle(g2,c.x,c.y, (int) c.size);

                //  g2.setColor(Color.WHITE);
                //  drawCenteredCircle(g2,c.x,c.y, c.height+5);
                //  g2.setColor(c.color);
                //  drawCenteredCircle(g2,c.x,c.y, c.height);
                Main.pressed = (int) c.size;
                if (c.size > 36)
                    drawImage(g2, Main.preview, c.x, c.y, (int) c.size, 0.9f);

                drawImage(g2, Main.hit_background, c.x, c.y, c.height, 0.9f);
                drawImage(g2, Main.hit_overlay, c.x, c.y, c.height, 0.9f);


                if (preview.get(0) != null) {
                    RectangleObj k = preview.get(0);
                    if (now + 1 < frames.length) {
                        if (frames[now + 1] != null) {
                            String[] frame = frames[now + 1].split("~");
                            if (Integer.parseInt(frame[0]) < 30) {
                                g2.setColor(white);
                                //drawSetas(g2, Game.setas, c.x, c.y, k.x, k.y, 35);
                                g2.drawLine(c.x, c.y, k.x, k.y);
                            }
                        }
                    }
                    //  drawArrowLine(g, c.x,c.y,k.x,k.y, 20, 0);
                }

                //  g2.rotate(Math.toRadians(-c.rotation),c.x+c.width/2,c.y+c.height/2);
            }
        }


     */
    public float getAngle(int baseX, int baseY, int x, int y) {
        float angle = (float) Math.toDegrees(Math.atan2(y - baseY, x - baseX));

        if (angle < 0)
            angle += 360;

        return angle;
    }

    public void drawSetas(Graphics2D g, Image image, int x1, int y1, int x2, int y2, int size) {
        x1 = x1 - (size / 2);
        y1 = y1 - (size / 2);

        x2 = x2 - (size / 2);
        y2 = y2 - (size / 2);


        int rotation = (int) getAngle(x1, y1, x2, y2);

        Main.affineTransform = AffineTransform.getTranslateInstance(x1, y1);

        Main.affineTransform.rotate(Math.toRadians(rotation), size / 2, size / 2);

        g.drawImage(image, Main.affineTransform, Main.game);


        //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
        // g.translate(-(40-40), 0);
        //g.rotate(Math.toRadians(rotation), 40/2, 40/2);
        //g.rotate(Math.toRadians(rotation), x1 + 40 /2, y1 + 40 /2);
        //g.drawImage(image,x1,y1,size, size, Game.game);
        //g.rotate(Math.toRadians(-rotation), x1 + 40 /2, y1 + 40 /2);
        // g.rotate(Math.toRadians(-rotation), 40/2, 40/2);
    }

    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }

}
