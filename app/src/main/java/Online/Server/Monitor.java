package Online.Server;

import java.io.Serializable;

import Online.OnlineActivities;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor extends OnlineActivities implements Serializable {
    private int turn;
    private int currentTurn;

    public Monitor() {
        turn = 1;
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
}
