package Online.Server;

import Online.GameThread;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor {

    private int turn;

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
        notifyAll();
    }

}
