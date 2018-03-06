package online;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import online.server.HostGame;
import se.lth.soc13dan.battleshipsedaf65.GameOver;
import se.lth.soc13dan.battleshipsedaf65.MainMenu;

/**
 * Created by otto on 2018-02-18.
 */

public class Monitor extends OnlineActivities implements Serializable {
    private int turn, score, enemyScore;
    private int target;
    public boolean myTurn, gameOver;
    private ArrayList<Integer> myPositions, enemyPositions;


    public boolean setupPhase;

    public Monitor(boolean myTurn) {
//        turn = 1;
        this.myTurn = myTurn;
        setupPhase = true;
        gameOver = false;
        enemyPositions = new ArrayList<>();
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
        fillEnemyBoard(mGrid, enemyPositions);

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
