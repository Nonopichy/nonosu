package com.nonopichy.nonosu.maker;

import com.nonopichy.nonosu.Main;
import com.nonopichy.nonosu.level.State;
import com.nonopichy.nonosu.util.sound.Mp3Magic;

import javax.imageio.ImageIO;
import javax.media.Time;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Maker {

    public static final Color Opacity = new Color(0, 0, 0, 10);
    public static BufferedImage background = null;

    public static Mp3Magic sound = new Mp3Magic("");
    public static Time position = new Time(0);

    public static ArrayList<Frame> frameList = new ArrayList<>();

    static {
        // Define background image
        try {
            background = ImageIO.read(new File("sys/maker.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Launcher Maker
    public static void init() {
        windowButton();
        endButton();
        stopButton();
        playButton();
        convertButton();
    }

    public static void convert() {
        BufferedWriter output = null;
        try {
            File file = new File("fase.txt");
            output = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < frameList.size(); i++) {
                if (i + 1 < frameList.size())
                    output.write(frameList.get(i).toString() + ";");
                else
                    output.write(frameList.get(i).toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Button maker in main menu!
    public static void windowButton() {
        addButton("Maker", 150, 50, 400, 10, e -> {
            Main.removeButtons();
            Main.state = State.MAKER;
            Main.visibleButton("End", true);
            Main.visibleButton("Stop", true);
            Main.visibleButton("Play", true);
            Main.visibleButton("Convert", true);
        });
    }

    // Button end for maker!
    public static void endButton() {
        addButton("End", 150, 25, 450, 450, e -> {
            Main.state = State.MENU;
            Main.showButtons();
            Main.changeVisibleButton("End");
        });
        Main.visibleButton("End", false);
    }

    // Button stop for maker!
    public static void stopButton() {
        addButton("Stop", 75, 25, 350, 450, e -> {
            position = sound.getPosition();

            sound.close();
        });
        Main.visibleButton("Stop", false);
    }

    // Button stop for maker!
    public static void playButton() {
        addButton("Play", 75, 25, 250, 450, e -> {
            if (!sound.isRunning())
                sound.play(position);
        });
        Main.visibleButton("Play", false);
    }

    // Button for convert file!
    public static void convertButton() {
        addButton("Convert", 75, 25, 150, 450, e -> {
            if (sound.isRunning())
                sound.close();
            convert();
        });
        Main.visibleButton("Convert", false);
    }

    // Add button
    public static void addButton(String name, int width, int heigth, int x, int y, ActionListener e) {
        JButton button = new JButton(name);
        button.setBounds(x, y, width, heigth);
        Main.list_buttons.add(button);
        Main.jframe.add(button);
        button.addActionListener(e);
    }

    public static void render(Graphics g) {
        renderBackground((Graphics2D) g);
        g.setColor(Color.RED);
        g.drawRect(54, 40, 1, 1);
        g.setColor(Color.RED);
        g.drawRect(Main.LARGURA - 54, Main.ALTURA - 40, 1, 1);
    }

    // Render background image
    private static void renderBackground(Graphics2D g2) {
        g2.drawImage(background, 0, 0, Main.LARGURA, Main.ALTURA, Main.game);
        g2.setColor(Opacity);
        g2.fillRect(0, 0, Main.LARGURA, Main.ALTURA);
    }


}
