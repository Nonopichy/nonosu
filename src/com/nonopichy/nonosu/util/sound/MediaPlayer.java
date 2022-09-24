package com.nonopichy.nonosu.util.sound;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MediaPlayer extends JPanel {
    public MediaPlayer(URL mediauUrl) {

        setLayout(new BorderLayout());

        try {


            Player mediaPlayer = Manager.createRealizedPlayer(new MediaLocator(mediauUrl));

            Component video = mediaPlayer.getVisualComponent();

            Component control = mediaPlayer.getControlPanelComponent();

            if (video != null) {

                add(video, BorderLayout.CENTER);          // place the video component in the panel

            }

            add(control, BorderLayout.SOUTH);            // place the control in  panel

            mediaPlayer.start();

        } catch (Exception e) {

        }

    }
}
