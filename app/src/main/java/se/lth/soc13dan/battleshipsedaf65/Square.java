package se.lth.soc13dan.battleshipsedaf65;

/**
 * Created by Daniel Andersson on 27-02-18.
 */

public class Square {
    private int id;
    private boolean press;
    private boolean isShip;
    private boolean isHit;

    public Square(int id, boolean isShip){
        this.id = id;
        press = false;
        this.isShip = isShip;
        this.isHit = false;
    }

    public int getCoord(){
        return id;
    }

    public void press() {
        press = !press;
    }

    public void hasShip(){
        isShip = true;
    }

    public void noShip() {
        isShip = false;
    }

    public boolean isPressed() {
        return press;
    }

    public boolean isShip() {
        return isShip;
    }

    public void hit() {
        isHit = true;
    }

    public boolean isHit() {
        return isHit;
    }
}
