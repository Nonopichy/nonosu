package com.nonopichy.nonosu.util.sound;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.media.Time;
import javax.swing.*;

public class Mp3Magic {
    private final String filename;
    private javax.media.bean.playerbean.MediaPlayer player;
    private boolean running;

    public Mp3Magic(String filename) {
        this.filename = filename;
    }

    public boolean isRunning() {
        return running;
    }

    public void close() {
        if (player != null) {
            player.stop();
        }
        running = false;
    }

    public void play() {
        play(new Time(0));
    }

    public Time getPosition() {
        return player.getMediaTime();
    }

    public void play(Time frame) {
        try {
            // This will prepare JavaFX toolkit and environment
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new JFXPanel();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            player = new javax.media.bean.playerbean.MediaPlayer();
                            player.setMediaLocation("file:///C:\\Users\\nonop\\OneDrive\\Desktop\\Projetos\\# SERVER (1.7.0 - 1.8.9)\\plugins\\#\\sound.wav".replaceAll(" ", "%20").replace("\\", "/"));
                            player.realize();


                            if (frame.getSeconds() != 0) {
                                new Thread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(10);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                player.setMediaTime(frame);
                                                player.start();
                                                running = true;
                                            }
                                        }
                                ).start();
                            } else {
                                running = true;
                                player.start();
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {
            running = false;
            e.printStackTrace();
        }
    }
}