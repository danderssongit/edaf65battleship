package Online.Server;

import Online.GameThread;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor {

    private int turn;
    private int currentTurn;

    public Monitor() {
        turn = 1;
    }

    public synchronized int waitTurn(GameThread c) {
        while (!(c.id == turn)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return turn;
    }



    public synchronized void changeTurn() {
        turn = (turn + 1) % 2 // swaps between 1 and 0
        notifyAll();
    }

}
