package online.client;

import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import online.Monitor;
import online.OnlineActivities;

import se.lth.soc13dan.battleshipsedaf65.R;


public class JoinGame extends OnlineActivities {
    private ClientThread client;
    private Monitor monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final GridLayout mGrid = (GridLayout) findViewById(R.id.grid_layout);
        monitor = (Monitor) getIntent().getSerializableExtra("monitor");
        client = new ClientThread(monitor, mGrid);
        client.start();

        final TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Click squares to position your ships!");
        System.out.println(mGrid);

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
                statusText.setText("");
                monitor.addMyPositions(getMyPositions());
                monitor.setupPhase = false;
                readyButton.setVisibility(View.GONE);
                gameStartButton.setVisibility(View.VISIBLE);
            }
        });



        setupPhase(mGrid, readyButton, true);
    }


    @Override
    public void finish() {
        client.interrupt();
        client.killSockets();
    }
}
