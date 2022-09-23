package com.nonopichy.nonosu;

import com.nonopichy.nonosu.Renders.RenderLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.nonopichy.nonosu.MyUtil.loadImagem;

public class Game extends Canvas implements Runnable, MouseListener, KeyListener {

    public static final int LARGURA = 620;
    public static final int ALTURA = 480;
    public static Level level = null;

    public static int mx, my;
    public static long pontuacao;
    public static int combo;
    public static List<JButton> list_buttons = new ArrayList<JButton>();

    public static JFrame jframe;
//    public static JPanel jpanel;

    public static State state = State.MENU;
    public static int pressed = 0;
    public static MP3 tap;
    public static MP3 combobreak;
    public static Image hit_overlay;
    public static Image hit_background;
    public static Image preview;
    public static Game game;
    public static Image setas;
    public static Image bad;
    public static Image normal;
    public static Image perfect;
    public static Image fail;
    public static Image fail_noclick;
    public static AffineTransform affineTransform = new AffineTransform();
    public static RenderLevel renderLevel;
    public static int ruins;
    public static int meidas;
    public static int boas;
    public static int falha;
    public static Robot bot;
    public static int life;
    public static Boolean clicked = false;
    public int FPS = 60;

    public Game() {

        Dimension dimension = new Dimension(LARGURA, ALTURA);
        setPreferredSize(dimension);
        addMouseListener(this);
        addKeyListener(this);

        bot = null;
        try {
            bot = new Robot();
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }

        renderLevel = new RenderLevel(LARGURA, ALTURA, null, 196);
    }

    public static void loadSoundsSys() {

        tap = new MP3("sys/tap.mp3");
        combobreak = new MP3("sys/combobreak.mp3");
    }

    public static void loadImagemSys() {
        hit_background = loadImagem("sys/hit_background.png");
        hit_overlay = loadImagem("sys/hit_overlay.png");
        preview = loadImagem("sys/preview.png");
        setas = loadImagem("sys/setas.png");
        fail = loadImagem("sys/fail.png");
        bad = loadImagem("sys/bad.png");
        normal = loadImagem("sys/normal.png");
        perfect = loadImagem("sys/perfect.png");
        fail_noclick = loadImagem("sys/fail_noclick.png");

    }

    public static void main(String[] args) {

        loadImagemSys();
        loadSoundsSys();

        Game game = new Game();
        Game.game = game;

        jframe = new JFrame("Quee");


        SwingUtilities.invokeLater(() -> {
            try {
                jframe.setCursor(MyUtil.getCursor());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loadLevels();
        jframe.add(game);

        jframe.setLocationRelativeTo(null);
        jframe.pack();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);

        new Thread(game).start();

    }

    public static void loadLevels() {
        int y = 10;
        for (File file : new File("levels").listFiles()) {
            JButton button = new JButton(file.getName());
            button.setBounds(10, y, 150, 50);
            list_buttons.add(button);
            jframe.add(button);
            button.addActionListener(e ->
            {
                level = new Level(file.getName());
                removeButtons();
                // JOptionPane.showMessageDialog(null,file.getName(),"Queé", JOptionPane.INFORMATION_MESSAGE);
            });
            y = y + 60;
        }
    }

    public static void removeButtons() {
        for (JButton b : list_buttons)
            b.setVisible(false);
    }

    public static void showButtons() {
        for (JButton b : list_buttons)
            b.setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            update();
            render();
            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        renderLevel.update();

    }

    public void renderEnd(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, LARGURA, ALTURA);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.ITALIC, 32));
        g.drawString("Pontuacao: " + pontuacao, 50, 50);
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.ITALIC, 32));
        g.drawString("Boas: " + boas, 50, 100);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.ITALIC, 32));
        g.drawString("Medias: " + meidas, 50, 150);
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.ITALIC, 32));
        g.drawString("Ruins: " + boas, 50, 200);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.ITALIC, 32));
        g.drawString("Falhas: " + falha, 50, 250);
        g.setFont(new Font("Arial", Font.ITALIC, 32));
        g.drawString("State: " + state, 50, 300);
    }

    public void renderMenu(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, LARGURA, ALTURA);
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        if (state == State.MENU)
            renderMenu(g);
        if (state == State.GAME)
            renderLevel.render(g);
        if (state == State.END)
            renderEnd(g);

        bs.show();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        if (state == State.END) {
            showButtons();
            state = State.MENU;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (clicked == true)
            return;
        if (e.getKeyCode() == 90 || e.getKeyCode() == 88 || e.getKeyCode() == 127 || e.getKeyCode() == 35) {
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            clicked = true;
        }
        // StringSelection selection = new StringSelection(">"+e.getKeyChar()+",>"+e.getKeyCode());
        //    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //    clipboard.setContents(selection, selection);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (clicked == true)
            clicked = false;
    }
}
