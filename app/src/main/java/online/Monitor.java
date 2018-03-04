package online;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor extends OnlineActivities implements Serializable {
    private int turn;
    private int target;
    private boolean myTurn;
    private ArrayList<Integer> myPositions, enemyPositions;


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

    public void addMyPositions(ArrayList<Integer> positions){
        this.myPositions = positions;
    }

    public String getSetupPositions() {
        String s = "";
        for(Integer pos : myPositions){
            s += pos.toString() + ":";
        }
        s += "*";
        System.out.println(s);
        return s;
    }

    public void addEnemyPositions(String positions){
        enemyPositions = new ArrayList<>();

        String[] posArray = positions.split(":");
        for(String pos : posArray) {
                enemyPositions.add(Integer.parseInt(pos));
        }
        System.out.println("ENEMY HAS SHIPS ON: " + enemyPositions);
        fillEnemyBoard(enemyPositions);

    }
}
