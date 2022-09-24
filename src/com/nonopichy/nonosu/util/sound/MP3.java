package com.nonopichy.nonosu.util.sound;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

@Deprecated
public class MP3 {
    private final String filename;
    private Player player;
    private AudioDevice audio;
    private boolean running = false;

    // constructor that takes the name of an MP3 file
    public MP3(String filename) {
        this.filename = filename;
    }

    public void close() {
        if (player != null) player.close();
    }

    // play the MP3 file to the sound card
    public void play() {
        play(10000);
    }

    public int getPosition() {
        return (running) ? player.getPosition() : 0;
    }

    public void play(int frame) {
        if (player == null) {
            try {
                FileInputStream fis = new FileInputStream(filename);
                BufferedInputStream bis = new BufferedInputStream(fis);
                player = new Player(bis);
                running = true;
            } catch (Exception e) {
                System.out.println("Problem playing file " + filename);
                System.out.println(e);
                running = false;
            }
        }
        // run in new thread to play in background
        new Thread() {
            public void run() {
                try {
                    if (frame != 0)
                        player.play(frame);
                    else
                        player.play();
                } catch (Exception e) {
                    System.out.println(e);
                    running = false;
                }
            }
        }.start();
    }
}