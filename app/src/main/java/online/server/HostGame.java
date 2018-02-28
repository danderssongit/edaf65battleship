package online.server;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayout;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import online.OnlineActivities;
import se.lth.soc13dan.battleshipsedaf65.ButtonListener;
import se.lth.soc13dan.battleshipsedaf65.DragListener;
import se.lth.soc13dan.battleshipsedaf65.R;


public class HostGame extends OnlineActivities {
    private HostThread host;
    private ServerThread server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Monitor monitor = new Monitor();
        final Handler turnHandler = new Handler();

        host = new HostThread(turnHandler, monitor);
        server = new ServerThread(monitor);
        host.start();
        server.start();

        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Place your ships!");

        GridLayout mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener(mGrid));

        Button shootButton = (Button) this.findViewById(R.id.angry_btn);
        shootButton.setOnClickListener(new ButtonListener());
        ArrayList<Integer> board = setupPhase(mGrid);

    }

    @Override
    public void finish() {
        host.interrupt();
        server.interrupt();
        server.killSockets();
        super.finish();
    }
}
