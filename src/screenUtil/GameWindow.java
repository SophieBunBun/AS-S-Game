package screenUtil;

import data.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow {

    private JFrame frame;
    private JLabel textDisplay;

    public GameWindow(GameData gameData) {
        frame = new JFrame("GameWindow");
        textDisplay = new JLabel();
        textDisplay.setOpaque(true);

        JScrollPane pane = new JScrollPane(textDisplay);
        frame.getContentPane().add(pane, BorderLayout.CENTER);

        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                gameData.setKeyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void setText(String text){
        textDisplay.setText(text);
    }

    public void close(){
        frame.dispose();
    }
}
