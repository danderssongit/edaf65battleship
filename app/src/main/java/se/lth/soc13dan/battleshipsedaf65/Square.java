package se.lth.soc13dan.battleshipsedaf65;

import android.view.View;

/**
 * Created by Daniel Andersson on 27-02-18.
 */

public class Square {
    int x;

    public Square(int x){
        this.x = x;
    }

    public String getCoord(){
        return Integer.toString(x);

    }
}
