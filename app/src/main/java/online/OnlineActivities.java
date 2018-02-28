package online;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

import online.server.Monitor;
import se.lth.soc13dan.battleshipsedaf65.LongPressListener;
import se.lth.soc13dan.battleshipsedaf65.R;
import se.lth.soc13dan.battleshipsedaf65.Square;

/**
 * Created by otto on 2018-02-21.
 */

public class OnlineActivities extends AppCompatActivity {
    public static final int NBR_ITEMS = 100;
    protected boolean yourTurn;
    private Square square;
    private ArrayList<Integer> board;
    private Monitor monitor;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        yourTurn = false;
//    }

    public OnlineActivities() {
        yourTurn = false;
//        monitor = new Monitor();      // causes crash..?
    }

    public ArrayList<Integer> setupPhase(GridLayout mGrid) {
        board = new ArrayList<>();
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; i <= NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    square = (Square) v.getTag();
                    if (!square.isPressed()) {
                        text.setText("X");
                        square.press();
                        board.add(square.getCoord());
                    }
                }
            });
            if (i > 90  && i < 98) {
                text.setText("S");
                itemView.setTag(new Square(i, true));
                itemView.setOnLongClickListener(new LongPressListener());
            } else {
                text.setText("O");
                itemView.setTag(new Square(i, false));
            }
            mGrid.addView(itemView);
        }
        return board;
    }

    public void checkForHit(int playerID, int squareID) {
        for(Square s : Monitor.board.get(playerID)){
            if ((s.getCoord() == squareID) && s.isShip()) {
                s.hit(); //set status to hit
            } else if ((s.getCoord() == squareID)){
                s.press(); //set to miss instead
            }
        }
        updateView(squareID);
        monitor.changeTurn();
    }

    public void updateView(int squareID) {

    }



}
