package online;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
    private ArrayList<Integer> positions;
    private int placedShips;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        yourTurn = false;
//    }

    public OnlineActivities() {
        yourTurn = false;
        hostBoard = new ArrayList<>();
        clientBoard = new ArrayList<>();
        positions = new ArrayList<>();
    }

    public void setupPhase(GridLayout mGrid, final TextView nbrShipsToPlace, final Button readyButton, int playerID, Boolean myTurn) {
        placedShips = 0;
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; i <= NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            text.setText("");
            square = new Square(i, false);
            itemView.setTag(square);
            whatBoard(playerID).add(square);

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    square = (Square) v.getTag();
                    Integer pos = new Integer(square.getCoord());
                    if (positions.size() < 3) {
                        if (!positions.contains(pos)) {
                            text.setText("S");
                            square.shipToggle();
                            positions.add(square.getCoord());
                            placedShips++;
                            nbrShipsToPlace.setText("Ships left to place: " + (3 - placedShips));
                            System.out.println(positions + ", ships placed: " + Integer.toString(placedShips) + ", positions size: " + positions.size());
                            if (placedShips == 3) {
                                readyButton.setEnabled(true);
                            }
                        } else {
                            text.setText("");
                            square.shipToggle();
                            positions.remove(pos);
                            placedShips--;
                            nbrShipsToPlace.setText("Ships left to place: " + (3 - placedShips));
                            System.out.println(positions + ", ships placed: " + Integer.toString(placedShips) + ", positions size: " + positions.size());
                            if (placedShips < 3) {
                                readyButton.setEnabled(false);
                            }
                        }
                    } else if (square.isShip()){
                        text.setText("");
                        square.shipToggle();
                        positions.remove(pos);
                        placedShips--;
                        nbrShipsToPlace.setText("Ships left to place: " + (3 - placedShips));
                        System.out.println(positions + ", ships placed: " + Integer.toString(placedShips) + ", positions size: " + positions.size());
                        if (placedShips < 3) {
                            readyButton.setEnabled(false);
                        }
                    }
                }
            });
            mGrid.addView(itemView);
        }

    }

//    public void setupPhase(GridLayout mGrid, int playerID, Boolean myTurn) {
//        monitor = new Monitor(myTurn);
//        final LayoutInflater inflater = LayoutInflater.from(this);
//        for (int i = 1; i <= NBR_ITEMS; i++) {
//            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
//            final TextView text = itemView.findViewById(R.id.text);
//            if (i > 90  && i < 98) { // Ship starting positions
//                text.setText("S");
//                square = new Square(i, true);
//                itemView.setTag(square);
//                itemView.setOnLongClickListener(new LongPressListener()); // Only cells containing ships should be movable
//            } else {
//                text.setText("");
//                square = new Square(i, false);
//                itemView.setTag(square);
//            }
//            whatBoard(playerID).add(square);
//            mGrid.addView(itemView);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    square = (Square) v.getTag();
//                    if (!square.isPressed() && monitor.isMyTurn()) {
//                        text.setText("o");
//                        square.press();
//                        monitor.changeTurn(square.getCoord());
//                        if (square.isShip()) {
//                            square.hit();
//                        }
//                    } else if(!monitor.isMyTurn()) {
//                        System.out.println("Not your turn...");
//                    }
//                }
//            });
//        }
//    }


    public void shoot(int squareID) {
        if (square.getCoord() == squareID) {
            square.pressToggle();
        }
    }

    public void addPositions() {
        monitor.addPositions(positions);
    }

//    public ArrayList<Integer> getSetupPositions(){
//        return positions;
//    }

    public void checkForHit(GridLayout mGrid, int playerID, int squareID) {
        for (Square square : whatBoard(playerID)) {
            if ((square.getCoord() == squareID) && square.isShip()) {
                square.hit(); //set status to hit
            } else if ((square.getCoord() == squareID)) {
                square.pressToggle(); //set to miss instead
            }
        }
        updateView(mGrid, whatBoard(playerID));
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
