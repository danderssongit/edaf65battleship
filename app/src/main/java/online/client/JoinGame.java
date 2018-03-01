package online.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

import online.OnlineActivities;
import se.lth.soc13dan.battleshipsedaf65.DragListener;
import se.lth.soc13dan.battleshipsedaf65.R;


public class JoinGame extends OnlineActivities {
    private ClientThread client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final Handler turnHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                System.out.println(msg.what);
            }
        };

        client = new ClientThread(turnHandler);
        client.start();

        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Place your ships!");
        GridLayout mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener(mGrid));

//        ArrayList<Integer> board = setupPhase(mGrid);

    }

    @Override
    public void finish() {
        client.interrupt();
        client.killSockets();
    }
}
