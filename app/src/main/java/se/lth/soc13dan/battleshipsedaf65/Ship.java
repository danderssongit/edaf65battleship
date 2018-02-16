package se.lth.soc13dan.battleshipsedaf65;

/**
 * Created by soc13dan on 2018-02-16.
 */

public class Ship {
    private int size;
    private int orientation;
    private int col, row;

    public Ship(int size, int orientation, int col, int row) {
        this.size = size;
        this.orientation = orientation;
        this.col = col;
        this.row = row;
    }


}
