package com.nonopichy.nonosu;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class MyUtil {
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

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void playVideo(String path) {
        final JFXPanel VFXPanel = new JFXPanel();


        File video_source = new File(path);
        Media m = new Media(video_source.toURI().toString());
        MediaPlayer player = new MediaPlayer(m);
        MediaView viewer = new MediaView(player);

        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        // center video position
        javafx.geometry.Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        viewer.setX((screen.getWidth() - Game.LARGURA) / 2);
        viewer.setY((screen.getHeight() - Game.ALTURA) / 2);

        // resize video based on screen size
        DoubleProperty width = viewer.fitWidthProperty();
        DoubleProperty height = viewer.fitHeightProperty();
        width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
        viewer.setPreserveRatio(true);

        // add video to stackpane
        root.getChildren().add(viewer);

        VFXPanel.setScene(scene);
        player.play();
        //  Game.jpanel.setLayout(new BorderLayout());
        //  Game.jpanel.add(VFXPanel, BorderLayout.CENTER);
    }

    public static Image loadImagem(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static Cursor getCursor() {
        Dimension bestSize = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
        Image image = Toolkit.getDefaultToolkit().getImage(new File("sys/mouse.png").getAbsolutePath());

        int centerX = bestSize.width / 2;
        int centerY = bestSize.height / 2;

        Point hotSpot = new Point(centerX, centerY);

        return Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot, "cursor");
    }

}
