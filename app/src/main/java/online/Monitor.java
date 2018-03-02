package online;

import java.io.Serializable;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor extends OnlineActivities implements Serializable {
    private int turn;
    private int target;
    private boolean myTurn;
    private int[] positions;

    public Monitor(boolean myTurn) {
//        turn = 1;
        this.myTurn = myTurn;
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

    public void addPositions(int[] positions){
        this.positions = positions;
    }

    public int[] getOponentPositions() {
        return positions;
    }
}
