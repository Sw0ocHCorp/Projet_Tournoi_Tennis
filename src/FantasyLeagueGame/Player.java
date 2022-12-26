package FantasyLeagueGame;

import java.awt.Image;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;

public class Player {
    int location;
    int yLocation= 0;
    int height= 10;
    int width;
    Image imgBackground;
    Graphics graphicsDrawer;
    Color colorPlayer= java.awt.Color.black;
    int playerNumber;
    JComponent componentEnv;
    double coeffLocation= 2;
    Random rand = new Random();

    public Player(Image imageSource, JComponent componentEnv) {
        this.imgBackground= imageSource;
        this.componentEnv= componentEnv;
        this.width = this.imgBackground.getWidth(null) / 5;
    }

    public void setup(Graphics graphicsDrawer) {
        this.graphicsDrawer = graphicsDrawer;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
        this.location= (this.imgBackground.getWidth(null) * 2) / 5;
        if (this.playerNumber == 1) {
            this.yLocation = 0;
            this.colorPlayer = java.awt.Color.blue;
        } else if (this.playerNumber == 2) {
            this.yLocation = this.imgBackground.getHeight(null)-13;
            this.colorPlayer = java.awt.Color.red;
        } else {
            System.out.println("Error: Player number must be 1 or 2");
        }
    }

    public void updateLocation() {
        if (coeffLocation < 0) {
            coeffLocation = 0;
        } else if (coeffLocation > 4) {
            coeffLocation = 4;
        }
        this.location= (int)(coeffLocation *this.imgBackground.getWidth(null)) / 5;
        graphicsDrawer.setColor(colorPlayer);
        graphicsDrawer.fillRect(this.location, this.yLocation, width, height);
    }

    public void reactionToBall(String direction) {
        Double[] rightMotionPossible= {0.0, 0.05, 0.1, 0.15, 0.2, -0.1, -0.15};
        Double[] leftMotionPossible= {0.0, -0.05, -0.1, -0.15, -0.2, 0.1, 0.15};
        double motion= 0;
        if (direction == "right") {
            motion= rightMotionPossible[rand.nextInt(rightMotionPossible.length)];
        } else if (direction == "left") {
            motion= leftMotionPossible[rand.nextInt(rightMotionPossible.length)];
        }
        coeffLocation += motion;
    }

    public int getLocation() {
        return this.location;
    }

    public int getWidth() {
        return this.width;   
    }
}
