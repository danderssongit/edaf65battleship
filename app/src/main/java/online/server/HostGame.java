package online.server;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    private static final int VICTORY = 1;
    private static final int DEFEAT = 2;
    private static final int SHOTRECEIVED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

//        monitor = (Monitor) getIntent().getSerializableExtra("monitor");

        final TextView statusText = (TextView) findViewById(R.id.status);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("GG", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                System.out.println("MESSAGE RECIEVED");
                switch (msg.what) {
                    case (VICTORY):
                        dialogBuilder.setMessage("Congratulations, you won! Your score: " + msg.arg1 + "! Enemy score: " + msg.arg2);
                        break;
                    case (DEFEAT):
                        dialogBuilder.setMessage("You lost, better luck next time! Your score: " + msg.arg1 + "! Enemy score: " + msg.arg2);
                        break;
                }
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }

        };

        final Monitor monitor = new Monitor(false, handler);
        server = new ServerThread(monitor, mGrid);
        server.start();

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
                gameStartButton.setVisibility(View.GONE);
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

        setupPhase(mGrid, readyButton, false);
    }

    @Override
    public void finish() {
        server.interrupt();
        server.killSockets();
        super.finish();
    }
}
