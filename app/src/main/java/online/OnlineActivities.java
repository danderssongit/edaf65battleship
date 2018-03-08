package online;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;

import se.lth.soc13dan.battleshipsedaf65.R;
import se.lth.soc13dan.battleshipsedaf65.Square;

/**
 * Created by otto on 2018-02-21.
 */

public class OnlineActivities extends AppCompatActivity {
    public static final int NBR_ITEMS = 36;
    public static final int NBR_SHIPS_TO_PLACE = 6;
    public static final int MY_ID = 0;
    public static final int ENEMY_ID = 1;
    protected boolean yourTurn;
    private Square square;
    public static ArrayList<Square> enemyBoard;
    public static ArrayList<Square> myBoard;
    private ArrayList<Integer> myPositions;
    private ArrayList<Integer> enemyPositions;
    private int placedShips, hits, attempt;


    private final int SHIP = 0x1F6A2;
    private final int MISS = 0x274C;
    private final int HIT = 0x1F525;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
    }

    public OnlineActivities() {
        yourTurn = false;
        enemyBoard = new ArrayList<>();
        myBoard = new ArrayList<>();
        myPositions = new ArrayList<>();
        enemyPositions = new ArrayList<>();
    }

    public void setupPhase(GridLayout mGrid, final Button readyButton, Boolean myTurn) {
        placedShips = 0;
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; i <= NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            text.setText("");
            square = new Square(i);
            itemView.setTag(square);
            whatBoard(MY_ID).add(square);
            readyButton.setText("Place " + (NBR_SHIPS_TO_PLACE - placedShips) + " more");

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    square = (Square) v.getTag();
                    Integer pos = new Integer(square.getCoord());
                    if (myPositions.size() < NBR_SHIPS_TO_PLACE) {
                        if (!myPositions.contains(pos)) {
                            text.setText(new String(Character.toChars(SHIP)));
                            square.shipToggle();
                            myPositions.add(square.getCoord());
                            placedShips++;

                        } else {
                            text.setText("");
                            square.shipToggle();
                            myPositions.remove(pos);
                            placedShips--;
                        }
                    } else if (square.isShip()) {
                        text.setText("");
                        square.shipToggle();
                        myPositions.remove(pos);
                        placedShips--;
                    }
                    System.out.println(myPositions + ", ships placed: " + Integer.toString(placedShips) + ", myPositions size: " + myPositions.size());
                    if (placedShips == NBR_SHIPS_TO_PLACE) {
                        readyButton.setText("READY");
                        readyButton.setEnabled(true);
                    } else {
                        readyButton.setText("Place " + (NBR_SHIPS_TO_PLACE - placedShips) + " more");
                        readyButton.setEnabled(false);
                    }
                }
            });
            mGrid.addView(itemView);
        }

    }

    public void gamePhase(final GridLayout mGrid, final Monitor monitor) {
        System.out.println("gamephase");
        mGrid.removeAllViews();
        mGrid.setVisibility(View.VISIBLE);
        System.out.println(monitor.getEnemyPositions());
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; i <= NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            text.setText("");
            square = new Square(i);
            if (monitor.getEnemyPositions().contains(i)) {
                square.putShip();
                System.out.println(i);
            }
            itemView.setTag(square);
            whatBoard(ENEMY_ID).add(square);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    square = (Square) v.getTag();
                    Integer pos = new Integer(square.getCoord());
                    checkForHit(monitor.getEnemyPositions(), pos, monitor);
                    updateView(mGrid, ENEMY_ID, monitor.getEnemyPositions(), monitor);

                }
            });
            mGrid.addView(itemView);
        }

    }

    public ArrayList<Integer> getMyPositions() {
        return myPositions;
    }


    public void checkForHit(ArrayList<Integer> enemyPositions, int pos, Monitor monitor) {
        if (enemyPositions.contains(pos) && !square.isPressed()) {
            hits++;
            if (hits == NBR_SHIPS_TO_PLACE) {
                System.out.println(hits);
                final TextView statusText = (TextView) findViewById(R.id.status);
                statusText.setText("Well done! Wait for opponent to finish..");
                monitor.registerScore(attempt);
            }
            square.hit();
            System.out.println("IT'S A HIT");
        } else if (!square.isPressed()) {
            System.out.println("IT'S A MISS");
        }
        square.setPressed();
    }

    public void updateView(final GridLayout mGrid, int playerId, final ArrayList<Integer> enemyPositions, final Monitor monitor) {
        mGrid.removeAllViews();
        ArrayList<Square> board = new ArrayList<>();
        board.addAll(whatBoard(playerId));
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (final Square sq : board) {
            this.square = sq;
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            itemView.setTag(square);

            if (playerId == ENEMY_ID) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        square = (Square) v.getTag();
                        if (!square.isPressed() && hits != NBR_SHIPS_TO_PLACE) {
                            attempt++;
                            System.out.println(attempt);
                            Integer pos = new Integer(square.getCoord());
                            checkForHit(enemyPositions, pos, monitor);
                            updateView(mGrid, ENEMY_ID, enemyPositions, monitor);
                        }
                    }
                });
                if (square.isHit()) {
                    text.setText(new String(Character.toChars(HIT)));
                } else if (square.isPressed()) {
                    text.setText(new String(Character.toChars(MISS)));
                }
            } else {
                if (square.isHit()) {
                    text.setText(new String(Character.toChars(HIT)));
                } else if (square.isShip()) {
                    text.setText(new String(Character.toChars(SHIP)));
                } else if (square.isPressed()) {
                    text.setText(new String(Character.toChars(MISS)));
                } else {
                    text.setText("");
                }
            }

            mGrid.addView(itemView);
        }

    }

    private ArrayList<Square> whatBoard(int playerID) {
        if (playerID == 1) {
            return enemyBoard;
        } else {
            return myBoard;
        }
    }
}
