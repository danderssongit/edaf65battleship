package se.lth.soc13dan.battleshipsedaf65;

/**
 * Created by soc13dan on 2018-02-16.
 */

public class Board {


    int[][] board = new int[10][10];


    /**
     * Creates a new 10x10 board.
     * 0 = water
     * 1 = ship
     * 2 = hit
     * 3 = miss
     * 4 = already tried
     */
    public Board() {

    }

    // kan inte returna först
    public int fire(int row, int col) {
        int bombs = 3;
        while (bombs != 0) {
            //miss
            if (board[row][col] == 0) {
                board[row][col] = 3;
                bombs--;
                return 3;
            }
            //hit
            if (board[row][col] == 1) {
                board[row][col] = 2;
                bombs--;
                return 2;
            }
            //already tried
            if (board[row][col] == 2 || board[row][col] == 3) {
                return 4;
            }

        }
        return 1;
    }
}