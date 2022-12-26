package FantasyLeagueGame;

import java.awt.Graphics;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TestAnimationPong extends JComponent {
    int x = 0;
    int y = 0;
    public TestAnimationPong() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        g.setColor(java.awt.Color.black);
        g.fillRect(x, y, 100, 10);

    }
    public static void main(String[] args) {
        TestAnimationPong test = new TestAnimationPong();
        JFrame frame = new JFrame();
        frame.setContentPane(test);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        frame.setVisible(true);
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
                test.x += 10;
                test.y +=10;
            } catch (Exception e) {
                // TODO: handle exception
            }
            test.repaint();
        }
    }
}

