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

        monitor = (Monitor) getIntent().getSerializableExtra("monitor");
        client = new ClientThread(monitor);
        client.start();

        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Click squares to position your ships!");
        final GridLayout mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener(mGrid));

        final Button readyButton = (Button) this.findViewById(R.id.angry_btn);
        readyButton.setEnabled(false);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateView(mGrid, OnlineActivities.hostBoard);
                monitor.addMyPositions(getPositions());
                monitor.setupPhase = false;
                readyButton.setText("SHOOT");
                gamePhase(mGrid, readyButton, PLAYER_ID, true);
            }
        });

        setupPhase(mGrid, readyButton, PLAYER_ID, true);
    }

    @Override
    public void finish() {
        client.interrupt();
        client.killSockets();
    }
}
