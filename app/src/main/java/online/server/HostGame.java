package online.server;

import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import online.OnlineActivities;

import online.Monitor;
import se.lth.soc13dan.battleshipsedaf65.DragListener;
import se.lth.soc13dan.battleshipsedaf65.R;


public class HostGame extends OnlineActivities {
    private final int PLAYER_ID = 0;
    private ServerThread server;
    private GridLayout mGrid;
    private Monitor monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
//        final Handler turnHandler = new Handler();
//        host = new HostThread(turnHandler, monitor);
//        host.start();

        monitor = (Monitor) getIntent().getSerializableExtra("monitor");
        server = new ServerThread(monitor, mGrid);
        server.start();

        final TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Click squares to position your ships!");

        mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener(mGrid));

        final Button gameStartButton = (Button) this.findViewById(R.id.ready_btn);
        gameStartButton.setText("START GAME");
        gameStartButton.setVisibility(View.GONE);
        gameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePhase(mGrid, gameStartButton, true, monitor.getEnemyPositions(), monitor);
            }
        });

        final Button readyButton = (Button) this.findViewById(R.id.angry_btn);
        readyButton.setEnabled(false);
        readyButton.setVisibility(View.VISIBLE);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateView(mGrid, OnlineActivities.enemyBoard);
                statusText.setText("");
                monitor.addMyPositions(getMyPositions());
                monitor.setupPhase = false;
                readyButton.setVisibility(View.GONE);
                gameStartButton.setVisibility(View.VISIBLE);
            }
        });

        setupPhase(mGrid, readyButton, false);
    }

    @Override
    public void finish() {
        server.interrupt();
        server.killSockets();
        super.finish();
    }
}
