package online.server;

import java.util.ArrayList;
import java.util.HashMap;

import online.OnlineActivities;
import se.lth.soc13dan.battleshipsedaf65.Square;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor extends OnlineActivities {

    private int turn;
    public static HashMap<Integer, ArrayList<Square>> board;
    private ArrayList<Square> myBoard, enemyBoard;
    private int target;
    private boolean myTurn;


    public Monitor() {
//        turn = 1;
        myTurn = false;
    }

    public void addBoard(ArrayList<Square> positions, int id) {
        board.put(id, positions);
    }


    public synchronized int waitTurn() {
        while (!myTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return target;

    }

    public synchronized void changeTurn(int i) {
        turn = (turn + 1) % 2; // swaps between 1 and 0
        target = i;
        myTurn = !myTurn;
        notifyAll();
    }


    public boolean isMyTurn() {
        return myTurn;
    }

    public void shoot(int squareID) {
//        checkForHit(turn, squareID);
    }
}
