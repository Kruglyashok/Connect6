package client;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

//gameStance == 1 --- my move, i send
//gameStance == 2 --- my opponets moves, i wait
//gameStance == 3 --- game is over
public class SendObject implements Serializable {
    private Point step1 = new Point(), step2 = new Point();
    private int gameStance = 0;
    Color sendColor;

    SendObject(Point step1, Point step2, int gameStance, Color myColor) {
    this.step1 = step1; 
    this.step2 = step2;
    this.gameStance = gameStance;
    this.sendColor = myColor;
    }
    public Point getStep1() {
    return step1;
    }
    public Point getStep2() {
    return step2;
    }
    public int getStance() {
    return gameStance;
    }
    public Color getColor() {
    return sendColor;
    }
}
