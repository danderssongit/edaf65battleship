package Online.Server;

import java.util.ArrayList;
import java.util.HashMap;

import Online.OnlineActivities;
import se.lth.soc13dan.battleshipsedaf65.Square;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor extends OnlineActivities {

    private int turn;
    private int currentTurn;
    public static HashMap<Integer, ArrayList<Square>> board;

    public Monitor() {
        turn = 1;
    }

    public void addBoard(ArrayList<Square> positions, int id) {
        board.put(id, positions);
    }


    public synchronized void waitTurn(int id, int squareID) {
        while (!(id == turn)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        shoot(squareID);
    }

    public synchronized void changeTurn() {
        turn = (turn + 1) % 2; // swaps between 1 and 0
        notifyAll();
    }

    public void shoot(int squareID) {
        checkForHit(turn, squareID);
    }
}
