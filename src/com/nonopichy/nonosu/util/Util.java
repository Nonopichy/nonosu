package com.nonopichy.nonosu.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class Util {

    public static final Random random = new Random();

    public static String readTxt(String path) {
        try {
            Scanner in = new Scanner(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            while (in.hasNext())
                sb.append(in.next());
            in.close();
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        return random.nextInt((max - min) + 1) + min;
    }

    public static Image loadImagem(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
    public static void a(){
       try {
        BufferedImage bi = ImageIO.read(
              new File("sys/mouse.png"));
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor c = toolkit.createCustomCursor(bi, new Point(0, 0), "img");
        Main.jframe.setCursor(c);
    } catch (IOException e) {
        e.printStackTrace();
    }}

     */

    public static Cursor getCursor() {
        Dimension bestSize = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
        Image image = Toolkit.getDefaultToolkit().getImage(new File("sys/transparente.png").getAbsolutePath());

        int centerX = bestSize.width / 2;
        int centerY = bestSize.height / 2;

        Point hotSpot = new Point(centerX, centerY);

        return Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot, "cursor");
    }

}
