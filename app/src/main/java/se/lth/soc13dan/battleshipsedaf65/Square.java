package se.lth.soc13dan.battleshipsedaf65;

/**
 * Created by Daniel Andersson on 27-02-18.
 */

public class Square {
    private int id;
    private boolean press = false;
    private boolean isShip = false;
    private boolean isHit = false;

    public Square(int id){
        this.id = id;
        press = false;
        isShip = false;
        this.isHit = false;
    }

    public int getCoord(){
        return id;
    }

    public void pressToggle() {
        press = !press;
    }

    public void setPressed(){
        press = true;
    }

    public void shipToggle(){
        isShip = !isShip;
    }

    public void putShip(){
        isShip = true;
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
