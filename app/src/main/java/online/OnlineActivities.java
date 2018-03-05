package online;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

import se.lth.soc13dan.battleshipsedaf65.R;
import se.lth.soc13dan.battleshipsedaf65.Square;

/**
 * Created by otto on 2018-02-21.
 */

public class OnlineActivities extends AppCompatActivity {
    public static final int NBR_ITEMS = 100;
    public static final int NBR_SHIPS_TO_PLACE = 5;
    protected boolean yourTurn;
    private Square square;
    public static ArrayList<Square> hostBoard;
    public static ArrayList<Square> clientBoard;
    private ArrayList<Integer> positions;
    private ArrayList<Integer> enemyPositions;
    private int placedShips;


    private final int SHIP = 0x1F6A2;
    private final int MISS = 0x274C;
    private final int HIT = 0x1F525;

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

    public void setupPhase(GridLayout mGrid, final Button readyButton, int playerID, Boolean myTurn) {
        placedShips = 0;
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; i <= NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            text.setText("");
            square = new Square(i);
            itemView.setTag(square);
            whatBoard(playerID).add(square);

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    square = (Square) v.getTag();
                    Integer pos = new Integer(square.getCoord());
                    if (positions.size() < NBR_SHIPS_TO_PLACE) {
                        if (!positions.contains(pos)) {
                            text.setText(new String(Character.toChars(SHIP)));
                            square.shipToggle();
                            positions.add(square.getCoord());
                            placedShips++;

                        } else {
                            text.setText("");
                            square.shipToggle();
                            positions.remove(pos);
                            placedShips--;
                        }
                    } else if (square.isShip()){
                        text.setText("");
                        square.shipToggle();
                        positions.remove(pos);
                        placedShips--;
                    }
                    System.out.println(positions + ", ships placed: " + Integer.toString(placedShips) + ", positions size: " + positions.size());
                    if (placedShips == NBR_SHIPS_TO_PLACE) {
                        readyButton.setText("Start Game");
                        readyButton.setEnabled(true);
                    } else {
                        readyButton.setText("Place " + (NBR_SHIPS_TO_PLACE  - placedShips) + " more");
                        readyButton.setEnabled(false);
                    }
                }
            });
            mGrid.addView(itemView);
        }

    }

    public void gamePhase(final GridLayout mGrid, final Button readyButton, int playerID, Boolean myTurn) {
        System.out.println("gamephase");
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; i <= NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            text.setText("");
            square = new Square(i);
            if (enemyPositions.contains(new Integer(i))) {
                square.putShip();
                System.out.println(i);
            }
            itemView.setTag(square);
            whatBoard(0).add(square);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    square = (Square) v.getTag();
                    Integer pos = new Integer(square.getCoord());
                    checkForHit(mGrid, 0, pos);
                }
            });
            mGrid.addView(itemView);
        }

    }

    public void shoot(int squareID) {
        if (square.getCoord() == squareID) {
            square.pressToggle();
        }
    }

    public ArrayList<Integer> getPositions(){
        return positions;
    }


    public void checkForHit(GridLayout mGrid, int playerID, int squareID) {
        for (Square square : whatBoard(playerID)) {
            if ((square.getCoord() == squareID) && square.isShip()) {
                square.hit(); //set status to hit
                System.out.println("IT'S A HIT");
            } else if ((square.getCoord() == squareID)) {
                square.pressToggle(); //set to miss instead
                System.out.println("IT'S A MISS");
            }
        }
//        updateView(mGrid, whatBoard(playerID));
    }

    public void updateView(ArrayList<Square> board) {
        mGrid.removeAllViews();
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (Square square : board) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            if (square.isHit()) {
                text.setText(new String(Character.toChars(HIT)));
            } else if (square.isShip()) {
                text.setText(new String(Character.toChars(SHIP)));
            } else if (square.isPressed()) {
                text.setText("O");
            } else {
                text.setText("");
            }
            mGrid.addView(itemView);
        }

    }

    public void fillEnemyBoard(ArrayList<Integer> positions){
//        for (Square square : whatBoard(0)){                 //TODO: Get enemy board
//            if(positions.contains(square.getCoord())){
//                square.putShip();
//            }
//        }
////        enemyPositions = new ArrayList<>(positions);
//        updateView(whatBoard(0));

        mGrid.removeAllViews();

//        final LayoutInflater inflater = LayoutInflater.from(this);
//        for (int i = 1; i <= NBR_ITEMS; i++) {
//            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
//            System.out.println("2");
//            final TextView text = itemView.findViewById(R.id.text);
//            text.setText("");
//            square = new Square(i);
//            if (positions.contains(new Integer(i))) {
//                square.putShip();
//                System.out.println(i);
//            }
//            itemView.setTag(square);
//            whatBoard(0).add(square);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    square = (Square) v.getTag();
//                    Integer pos = new Integer(square.getCoord());
//                    checkForHit(mGrid, 0, pos);
//                }
//            });
////            mGrid.addView(itemView);
//        }
    }

    private ArrayList<Square> whatBoard(int playerID) {
        if (playerID == 0) {
            return hostBoard;
        } else {
            return clientBoard;
        }
    }
}
