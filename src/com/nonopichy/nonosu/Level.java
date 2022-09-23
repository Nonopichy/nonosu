package com.nonopichy.nonosu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class Level {

    public static Color white = new Color(255, 255, 255, 131);
    public int timer = 0;
    public List<RectangleObj> rec = new ArrayList<>();
    public List<RectangleObj> preview = new ArrayList<>();
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

    public Level(String level) {
        Game.state = State.GAME;

        try {
            frames = MyUtil.readTxt("levels/" + level + "/notas.txt").split(";");
            sound = new MP3("levels/" + level + "/sound.mp3");
            File file = new File("levels/" + level + "/background.png");
            if (file.exists())
                background = ImageIO.read(new File("levels/" + level + "/background.png"));
            Game.pontuacao = 0;
            Game.combo = 0;
            Game.ruins = 0;
            Game.meidas = 0;
            Game.boas = 0;
            Game.falha = 0;

            ready = true;

            Game.renderLevel.setLevel(this);
            sound.play();


            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // MyUtil.playVideo("levels/"+level+"/background.mp4");
        // Game.jpanel.setVisible(true);

    }

    public void update() {
        timer++;

        if (preview.size() > 2)
            preview.remove(0);
        if (rec.size() > 1) {
            Game.life = Game.life - 10;
            acerto.add(new Acerto(Game.fail_noclick, rec.get(0).x, rec.get(0).y, rec.get(0).height, 0.9f));
            rec.remove(0);
            if (Game.combo > 0)
                Game.combobreak.play();
            Game.combo = 0;
            Game.falha++;
        }

        if (now < frames.length) {
            real_frames = frames[now].split("~");
            if (timer >= Integer.parseInt(real_frames[0])) {
                timer = 0;
                rec.add(new RectangleObj(Integer.parseInt(real_frames[1]), Integer.parseInt(real_frames[2]), 40, 40, c_clicker));
                if (now + 1 < frames.length) {
                    real_frames = frames[now + 1].split("~");
                    preview.add(new RectangleObj(Integer.parseInt(real_frames[1]), Integer.parseInt(real_frames[2]), 40, 60, c_preview_1));
                }
                if (now + 2 < frames.length) {
                    Game.life = -2;
                    real_frames = frames[now + 2].split("~");
                    preview.add(new RectangleObj(Integer.parseInt(real_frames[1]), Integer.parseInt(real_frames[2]), 40, 60, c_preview_1));

                }
                real_frames = frames[now].split("~");
                now++;
            }
        } else if (timer >= 120) {
            //   JOptionPane.showMessageDialog(null,"End","Queé", JOptionPane.INFORMATION_MESSAGE);
            sound.close();
            Game.state = State.END;
            Game.renderLevel.setLevel(null);//   Game.renderLevel = null;
            //    Game.end = true;
        }

        n = Integer.parseInt(real_frames[0]);

        for (int i = 0; i < rec.size(); i++) {
            RectangleObj c = rec.get(i);
            rec.get(i).update(n, false);

            if (Game.mx >= c.x_center && Game.mx < c.x_center + c.width
                    && Game.my >= c.y_center && Game.my < c.y_center + c.height) {

                //40 / 80 - 10 = 70
                if (timer >= Integer.parseInt(real_frames[0]) / 2) {
                    acerto.add(new Acerto(Game.normal, c.x, c.y, c.height, 0.9f));
                    Game.pontuacao = Game.pontuacao + 150;
                    Game.life = Game.life + 7;
                    Game.meidas++;
                } else if (timer >= Integer.parseInt(real_frames[0]) / 3) {
                    acerto.add(new Acerto(Game.perfect, c.x, c.y, c.height, 0.9f));
                    Game.pontuacao = Game.pontuacao + 300;
                    Game.life = Game.life + 10;
                    Game.boas++;
                } else {
                    acerto.add(new Acerto(Game.bad, c.x, c.y, c.height, 0.9f));
                    Game.pontuacao = Game.pontuacao + 50;
                    Game.life = Game.life + 5;
                    Game.ruins++;
                }


                Game.tap.play();
                Game.combo++;
                //animationstrs.add(new AnimationString(c.x,c.y,28, 5,Color.GREEN, timer+"/"+real_frames[0]));
                rec.remove(c);

                goal = true;

            }
        }

        for (int i = 0; i < preview.size(); i++) {
            RectangleObj c = preview.get(i);

            // preview.get(i).update(n*2+i);
            preview.get(i).update(n * 2 + i, true);

            if (c.opacity < 0.1f)
                preview.remove(c);
            if (Game.mx >= c.x_center && Game.mx < c.x_center + c.width
                    && Game.my >= c.y_center && Game.my < c.y_center + c.height) {
                if (Game.combo > 0)
                    Game.combo = 0;
                preview.remove(c);
                if (rec.size() > 1)
                    rec.remove(0);
                acerto.add(new Acerto(Game.bad, c.x, c.y, c.height, 0.9f));
                Game.pontuacao = Game.pontuacao + 50;
                Game.life = Game.life + 5;
                Game.ruins++;
                now++;
            }
/*
            if(Game.mx >= c.x && Game.mx < c.x + c.width
                    && Game.my >= c.y && Game.my < c.y + c.height) {

                Game.pontuacao ++;
                Game.combo = 0;
                animationstrs.add(new AnimationString(c.x,c.y,28, 5,Color.RED, "<>"));
                preview.remove(c);

                goal = true;

            }

 */
        }

        for (int i = 0; i < acerto.size(); i++) {
            Acerto c = acerto.get(i);
            acerto.get(i).update();
            if (c.opacity < 0.1f)
                acerto.remove(c);
        }
/*
        if(goal==false){
            if(Game.mx != 99999 &&
                    Game.my != 99999){
                if(rec.size()>0) {
                    acerto.add(new Acerto(Game.fail, rec.get(0).x,rec.get(0).y, rec.get(0).height, 0.9f));
                    rec.remove(0);
                    if(Game.combo > 0)
                        Game.combobreak.play();
                    Game.combo = 0;

                }
            }
        }

 */

        goal = false;

        Game.mx = 99999;
        Game.my = 99999;
    }

    public void render(Graphics g) {
        renderPreview(g);
        renderClicker(g);
        renderAnimationStrings(g);
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
        for (int i = 0; i < preview.size(); i++) {
            RectangleObj c = preview.get(i);
            Graphics2D g2 = (Graphics2D) g;
            //  g2.rotate(Math.toRadians(c.rotation),c.x+c.width/2,c.y+c.height/2);
            //  g2.setColor(c.color);
            //  drawCenteredOvalCircle(g2,c.x,c.y, (int) c.size);
            //  g2.rotate(Math.toRadians(-c.rotation),c.x+c.width/2,c.y+c.height/2);
            drawImage(g2, Game.preview, c.x, c.y, (int) c.size, c.opacity);
        }
    }

    public void drawImage(Graphics2D g, Image image, int x, int y, int r, float opacity) {
        x = x - (r / 2);
        y = y - (r / 2);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.drawImage(image, x, y, r, r, Game.game);
    }

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
            Game.pressed = (int) c.size;
            if (c.size > 36)
                drawImage(g2, Game.preview, c.x, c.y, (int) c.size, 0.9f);

            drawImage(g2, Game.hit_background, c.x, c.y, c.height, 0.9f);
            drawImage(g2, Game.hit_overlay, c.x, c.y, c.height, 0.9f);


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

        Game.affineTransform = AffineTransform.getTranslateInstance(x1, y1);

        Game.affineTransform.rotate(Math.toRadians(rotation), size / 2, size / 2);

        g.drawImage(image, Game.affineTransform, Game.game);


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
