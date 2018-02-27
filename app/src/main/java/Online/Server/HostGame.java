package Online.Server;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import Online.OnlineActivities;
import se.lth.soc13dan.battleshipsedaf65.DragListener;
import se.lth.soc13dan.battleshipsedaf65.LongPressListener;
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
        statusText.setText("Awaiting opponent..");
        GridLayout mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener(mGrid));

        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < OnlineActivities.NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            itemView.setOnLongClickListener(new LongPressListener());
            text.setText(String.valueOf(i + 1));
            mGrid.addView(itemView);
        }
    }

    @Override
    public void finish() {
        host.interrupt();
        server.interrupt();
        server.killSockets();
        super.finish();
    }
}

