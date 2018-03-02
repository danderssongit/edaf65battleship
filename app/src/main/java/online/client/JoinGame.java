package online.client;

import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import online.Monitor;
import online.OnlineActivities;

import se.lth.soc13dan.battleshipsedaf65.DragListener;
import se.lth.soc13dan.battleshipsedaf65.R;


public class JoinGame extends OnlineActivities {
    private final int PLAYER_ID = 1;
    private ClientThread client;
    private Monitor monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

//        final Handler turnHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                System.out.println(msg.what);
//            }
//        };

        monitor = (Monitor) getIntent().getSerializableExtra("monitor");
        client = new ClientThread(monitor);
        client.start();

        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Place your ships!");
        TextView shipsToPlaceText = (TextView) findViewById(R.id.shipsToPlace);
        shipsToPlaceText.setText("Ships left to place: 3");
        GridLayout mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener(mGrid));

        Button readyButton = (Button) this.findViewById(R.id.angry_btn);
        readyButton.setEnabled(false);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateView(mGrid, OnlineActivities.hostBoard);
                addPositions();
                monitor.setupPhase = false;
            }
        });

        setupPhase(mGrid, shipsToPlaceText, readyButton, PLAYER_ID, true);
    }

    @Override
    public void finish() {
        client.interrupt();
        client.killSockets();
    }
}
