package FantasyLeagueGame;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PongEngine extends JComponent {
    private Image imgBackground;
    private Player player1, player2;
    private Ball ball;
    double coeffLocation= 0.5;
    boolean isGameStarted= false;
    double scoreJ1= 0, scoreJ2= 0;

    public PongEngine() {
        
    }
    
    public void initEnv(Image imageSource) {
        this.imgBackground= imageSource;
        this.player1 = new Player(imageSource, this);
        this.player1.setPlayerNumber(1);
        this.player2 = new Player(imageSource, this);
        this.player2.setPlayerNumber(2);
        this.ball = new Ball(imageSource);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        g.drawImage(this.imgBackground, 0, 0, null);   
        this.player1.setup(g);
        this.player2.setup(g);
        this.ball.setup(g);
        isGameStarted = true;
        this.ball.updateLocation();
        this.player1.updateLocation();
        this.player2.updateLocation();
    }

    public int getBallXLocation() {
        return ball.prevXLocation;
    }
    public int getBallYLocation() {
        return ball.prevYLocation;
    }
    public int getPlayer1XLocation() {
        return player1.getLocation();
    }
    public int getPlayer2XLocation() {
        return player2.getLocation();
    }
    public String getBallDirection() {
        return ball.getDirection();
    }

    public int getPlayerWidth() {
        return player1.getWidth();
    }

    public ArrayList<Integer> getBallCaracteristics() {
        return ball.getBallCaracteristics();
    }

    public void setScore(String player) {
        if (player.equals("J1")) {
            scoreJ1+=0.5;
        } else if (player.equals("J2")) {
            scoreJ2+=0.5;
        }
        ball.setGoal();
    }

    public void invokePlayerReaction(String player, String direction) {
        if (player.equals("J1")) {
            player1.reactionToBall(direction);
        } else if (player.equals("J2")) {
            player2.reactionToBall(direction);
        }
    }

    public void verifyScore() {
        int ballXLocation = getBallXLocation();
        int player1XLocation = getPlayer1XLocation();
        int player2XLocation = getPlayer2XLocation();
        String ballDirection = getBallDirection();
        int playerWidth= getPlayerWidth();
        ArrayList<Integer> ballCaracteristics= getBallCaracteristics();

        if (ballDirection == "toJ2") {
            if ((getBallYLocation() + ballCaracteristics.get(1) + 10) > imgBackground.getHeight(null) - 13) {
                if (((ballXLocation - player2XLocation) < playerWidth)
                        && ((ballXLocation - player2XLocation) > -ballCaracteristics.get(0))) {
                } else if ((((ballXLocation + ballCaracteristics.get(0)) - player2XLocation) < playerWidth)
                        && (((ballXLocation + ballCaracteristics.get(0)) - player2XLocation) > 0)) {

                } else {
                    setScore("J1");
                    System.out.println("J1 Scored= " + scoreJ1);
                }
            }
        } else if (ballDirection == "toJ1") {
            if ((getBallYLocation() - 10) < 10) {
                if (((ballXLocation - player1XLocation) < playerWidth)
                        && ((ballXLocation - player1XLocation) > -ballCaracteristics.get(0))) {

                } else if ((((ballXLocation + ballCaracteristics.get(0)) - player1XLocation) < playerWidth)
                        && (((ballXLocation + ballCaracteristics.get(0)) - player1XLocation) > 0)) {

                } else {
                    setScore("J2");
                    System.out.println("J2 Scored= " + scoreJ2);
                }
            }
        }
    }

    public void playerReaction() {
        Double[] rightMotionPossible= {0.01, 0.03, 0.05, 0.07, -0.04, -0.06};
        Double[] leftMotionPossible= {-0.01, -0.03, -0.05, -0.07, 0.04, 0.06};
        String direction= "left";
        int playerSelected;
        int playerXLocation;
        int ballXLocation = getBallXLocation();
        String ballDirection = getBallDirection();
        int playerWidth= getPlayerWidth();
        Random rand = new Random();
        ArrayList<Integer> ballCaracteristics= getBallCaracteristics();

        if (ballDirection == "toJ2") {
            playerSelected = 2;
            playerXLocation = getPlayer2XLocation();
        } else {
            playerSelected = 1;
            playerXLocation = getPlayer1XLocation();
        }

        if (((ballXLocation - playerXLocation) > playerWidth)
            || (((ballXLocation + ballCaracteristics.get(0)) - playerXLocation) > playerWidth)) {
                direction= "right";
        } else if (((ballXLocation - playerXLocation) < -ballCaracteristics.get(0))
                || (((ballXLocation + ballCaracteristics.get(0)) - playerXLocation) < 0)) {
                direction= "left";
        }
        
        if (playerSelected == 1) {
            invokePlayerReaction("J1",direction);
        } else {
            invokePlayerReaction("J2",direction);
        }
    }

    public void playGame() {
        while (scoreJ1 < 5.0 || scoreJ2 < 5.0) {
            verifyScore();
            this.repaint();
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {
                // TODO: handle exception
            }
            //Elements Caracteristics
            verifyScore();
            //Player Reaction
            playerReaction();
            this.repaint();
        }
        
    }

    public static void main(String[] args) {
        //INITIALISATION DE LA FENETRE / JEU
        JFrame frame = new JFrame();
        PongEngine env= new PongEngine();
        int bgWidth= 10, bgHeight= 10;
        try {
            Image img= ImageIO.read(new File("C:\\Users\\nclsr\\OneDrive\\Bureau\\Cours_L3IA\\Base_de_Donnees_NoSQL\\Projet_Tournoi_Tennis\\src\\FantasyLeagueGame\\Tennis_Background.png"));
            env.initEnv(img);
            bgWidth= img.getWidth(null);
            bgHeight= img.getHeight(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        frame.setContentPane(env);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(bgWidth+15, bgHeight+35);
        frame.setVisible(true);

        //BOUCLE DE JEU
        env.playGame();
        
    }
}
