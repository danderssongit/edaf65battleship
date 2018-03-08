package online;

import java.io.Serializable;
import java.util.ArrayList;
import android.os.Message;
import android.support.v7.widget.GridLayout;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor extends OnlineActivities implements Serializable {
    private int score, enemyScore;
    public boolean myTurn, gameOver;
    private ArrayList<Integer> myPositions, enemyPositions;


    public boolean setupPhase;

    public Monitor(boolean myTurn) {
        this.myTurn = myTurn;
        setupPhase = true;
        gameOver = false;
        enemyPositions = new ArrayList<>();
    }


    public void registerScore(int score) {
        this.score = score;
        gameOver = true;
    }

    public String getScore() {
        return Integer.toString(score) + "*";
    }

    public int getScoreInt() {
        return score;
    }

    public void addMyPositions(ArrayList<Integer> positions) {
        this.myPositions = positions;
    }

    public String getSetupPositions() {
        String s = "";
        for (Integer pos : myPositions) {
            s += pos.toString() + ":";
        }
        s += "*";
        System.out.println(s);
        return s;
    }

    public void addEnemyPositions(GridLayout mGrid, String positions) {
        enemyPositions = new ArrayList<>();

        String[] posArray = positions.split(":");
        for (String pos : posArray) {
            enemyPositions.add(Integer.parseInt(pos));
        }
        System.out.println("ENEMY HAS SHIPS ON: " + enemyPositions);
    }

    public ArrayList<Integer> getEnemyPositions() {
        return enemyPositions;
    }

    public void setEnemyScore(int score) {
        enemyScore = score;
        compareScore();
    }

    public void compareScore() {
        Message msg = Message.obtain();
        msg.arg1 = score;
        msg.arg2 = enemyScore;

        if (enemyScore < score) {
            msg.what = 2;
        } else {
            msg.what = 1;
        }
    }
}
