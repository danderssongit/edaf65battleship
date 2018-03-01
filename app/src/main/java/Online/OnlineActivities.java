package Online;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Online.Server.Monitor;
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
    public static ArrayList<Square> hostBoard;
    public static ArrayList<Square> clientBoard;
    private Monitor monitor;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        yourTurn = false;
//    }

    public OnlineActivities() {
        yourTurn = false;
        hostBoard = new ArrayList<>();
        clientBoard = new ArrayList<>();
//        monitor = new Monitor();      // causes crash..?
    }

    public void setupPhase(GridLayout mGrid, int playerID) {
        ArrayList<Square> board = new ArrayList<>();
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; i <= NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            if (i > 90  && i < 98) { // Ship starting positions
                text.setText("S");
                square = new Square(i, true);
                itemView.setTag(square);
                itemView.setOnLongClickListener(new LongPressListener()); // Only cells containing ships should be movable
            } else {
                text.setText("");
                square = new Square(i, false);
                itemView.setTag(square);
            }
            whatBoard(playerID).add(square);
            mGrid.addView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    square = (Square) v.getTag();
                    if (!square.isPressed()) {
                        text.setText("o");
                        square.press();
                        if (square.isShip()) {
                            square.hit();
                        }
                    }
                }
            });
        }
    }

    public void shoot(int squareID) {
        if (square.getCoord() == squareID) {
            square.press();
        }
    }

    public void checkForHit(GridLayout mGrid, int playerID, int squareID) {
        for(Square square : whatBoard(playerID)){
            if ((square.getCoord() == squareID) && square.isShip()) {
                square.hit(); //set status to hit
            } else if ((square.getCoord() == squareID)){
                square.press(); //set to miss instead
            }
        }
        updateView(mGrid, whatBoard(playerID));
        monitor.changeTurn();
    }

    public void updateView(GridLayout mGrid, ArrayList<Square> board) {
        mGrid.removeAllViews();
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (Square square : board) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            if (square.isHit()) {
                text.setText("X");
            } else if (square.isShip()) {
                text.setText("S");
            } else if (square.isPressed()) {
                text.setText("O");
            } else {
                text.setText("");
            }
            mGrid.addView(itemView);
        }
    }

    private ArrayList<Square> whatBoard(int playerID) {
        if (playerID == 0) {
            return hostBoard;
        } else {
            return clientBoard;
        }
    }
}
