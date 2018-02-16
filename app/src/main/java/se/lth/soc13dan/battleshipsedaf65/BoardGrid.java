package se.lth.soc13dan.battleshipsedaf65;

/**
 * Created by soc13dan on 2018-02-16.
 */

public class BoardGrid {
    private int[][] board;

    public BoardGrid(int rows, int cols) {
        board = new int[rows][cols];
    }

    public boolean isGuessed(int row, int col) {
        return true;
    }

    public boolean hasShip(int row, int col) {
        return true;
    }

    public boolean isHit(int row, int col) {
        return true;
    }

    public int hit(int row, int col) {

    }
}
