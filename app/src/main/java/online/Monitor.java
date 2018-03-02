package online;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor extends OnlineActivities implements Serializable {
    private int turn;
    private int target;
    private boolean myTurn;
    private ArrayList<Integer> positions;
    public boolean setupPhase;

    public Monitor(boolean myTurn) {
//        turn = 1;
        this.myTurn = myTurn;
        setupPhase = true;
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

    public void addPositions(ArrayList<Integer> positions){
        this.positions = positions;
    }

    public String getSetupPositions() {
        String s = "";
        for(Integer pos : positions){
            s += pos.toString() + ":";
        }

        System.out.println(s);
        return s;


    }
}
