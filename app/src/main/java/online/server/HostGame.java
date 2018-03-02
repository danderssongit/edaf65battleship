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
    private HostThread host;
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
        server = new ServerThread(monitor);
        server.start();

        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Place your ships!");

        mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener(mGrid));
        setupPhase(mGrid, PLAYER_ID, false);

        Button shootButton = (Button) this.findViewById(R.id.angry_btn);
        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateView(mGrid, OnlineActivities.hostBoard);
                addPositions();
                monitor.setupPhase = false;
            }
        });

    }

    @Override
    public void finish() {
        host.interrupt();
        server.interrupt();
        server.killSockets();
        super.finish();
    }
}
