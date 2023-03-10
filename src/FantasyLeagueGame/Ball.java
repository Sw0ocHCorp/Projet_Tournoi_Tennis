package FantasyLeagueGame;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
// --> CLASSE: Modélisation de la balle du Jeu Pong
public class Ball {
    int prevXLocation = 0;
    int prevYLocation = 0;
    int xLocation= 0;
    int yLocation= 0;
    int height= 10;
    int width= 10;
    Image imgBackground;
    Graphics graphicsDrawer;
    Color colorBall= java.awt.Color.black;
    String direction= "toJ2";
    int xMotion= 0;
    int yMotion= 10;
    Random rand = new Random();
    boolean isGoal= false;

    // --> Constructeur: Création d'une balle et attachement à l'environnement de jeu
    public Ball(Image imageSource) {
        this.imgBackground= imageSource;
        this.width = this.imgBackground.getWidth(null) / 40;
        this.height = this.imgBackground.getWidth(null) / 40;
        this.prevXLocation= (int)((this.imgBackground.getWidth(null) * 19.5) / 40);
        this.prevYLocation= (int)((this.imgBackground.getHeight(null) * 19.5) / 40);
    }
    // --> Méthode d'attribution de l'outil de dessin de la balle
    public void setup(Graphics graphicsDrawer) {
        this.graphicsDrawer = graphicsDrawer;
        graphicsDrawer.setColor(colorBall);
    }
    // --> Méthode de MAJ de la position de la balle / Déplacement de la balle
    public void updateLocation() {

        if (this.prevXLocation + this.width + xMotion > this.imgBackground.getWidth(null)) {
            wallCollision();
        } else if (this.prevXLocation + xMotion <= 0) {
            wallCollision();
        }else {
            this.prevXLocation= prevXLocation + xMotion;
        }
        if (this.prevYLocation + this.height + yMotion > this.imgBackground.getHeight(null)-13) {
            if (isGoal == true) {
                isGoal= false;
                this.prevXLocation= (int)((this.imgBackground.getWidth(null) * 19.5) / 40);
                this.prevYLocation= (int)((this.imgBackground.getHeight(null) * 19.5) / 40);
                this.xMotion= 0;
            } else {
                playerCollision();
            }
        } else if (this.prevYLocation - yMotion < 0) {
            if (isGoal == true) {
                isGoal= false;
                this.prevXLocation= (int)((this.imgBackground.getWidth(null) * 19.5) / 40);
                this.prevYLocation= (int)((this.imgBackground.getHeight(null) * 19.5) / 40);
                this.xMotion= 0;
            } else {
                playerCollision();
            }
        }else {
            if (this.direction == "toJ2") {
                this.prevYLocation= prevYLocation + yMotion;
            } else if (this.direction == "toJ1") {
                this.prevYLocation= prevYLocation - yMotion;
            }
        }
        graphicsDrawer.fillRect(this.prevXLocation, this.prevYLocation, this.width, this.width);
    }
    // --> Méthode de gestion de la collision avec un joueur
    public void playerCollision() {
        int[] xMotionPossible= {-10, -8, -6, -4, -2, 0, 2, 4, 6, 8, 10};
        xMotion= xMotionPossible[rand.nextInt(xMotionPossible.length)]; 
        if (this.direction == "toJ2") {
            this.direction = "toJ1";
            this.prevYLocation= prevYLocation - 2*yMotion;
        } else if (this.direction == "toJ1") {
            this.direction = "toJ2";
            this.prevYLocation= prevYLocation + 2*yMotion;
        }
        this.prevXLocation= prevXLocation + xMotion;
    }
    // --> Méthode de gestion de la collision avec un mur
    public void wallCollision() {
        if (this.direction == "toJ2") {
            this.prevYLocation= prevYLocation + 2*yMotion;
        } else if (this.direction == "toJ1") {
            this.prevYLocation= prevYLocation - 2*yMotion;
        }
        xMotion= -xMotion;
    }
    // --> Récupération de la position sur l'axe X de la balle
    public int getXLocation() {
        return this.prevXLocation;
    }
    // --> Récupération des caractéristiques de la balle
    public ArrayList<Integer> getBallCaracteristics() {
        ArrayList<Integer> ballCaracteristics= new ArrayList<>();
        ballCaracteristics.add(this.width);
        ballCaracteristics.add(this.height);
        return ballCaracteristics;
    }
    // --> Récupération de la position sur l'axe Y de la balle
    public int  getBallYLocation() {
        return this.prevYLocation;
    }
    // --> Récupération de la direction de la balle
    public String getDirection() {
        return this.direction;
    }
    // --> Indication de la balle comme étant dans la zone de but pour Reset de sa position
    public void setGoal() {
        this.isGoal= true;
    }

}
