package Online.Client;

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


public class JoinGame extends OnlineActivities {
    private ClientThread client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final Handler turnHandler = new Handler();
        client = new ClientThread(turnHandler);
        client.start();

        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Position your ships");
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
        client.interrupt();
        client.killSockets();
    }
}
