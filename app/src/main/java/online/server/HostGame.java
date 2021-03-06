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
    private ServerThread server;
    private GridLayout mGrid;
    private Monitor monitor;

    private static final int VICTORY = 1;
    private static final int DEFEAT = 2;
    private static final int SETUPRECEIVED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        final TextView statusText = (TextView) findViewById(R.id.status);
        final Button readyButton = (Button) this.findViewById(R.id.angry_btn);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final Monitor monitor = new Monitor(false);
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
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(true);
                        alertDialog.show();
                        break;
                    case (DEFEAT):
                        dialogBuilder.setMessage("You lost, better luck next time! Your score: " + msg.arg1 + "! Enemy score: " + msg.arg2);
                        AlertDialog alertDialog2 = dialogBuilder.create();
                        alertDialog2.setCanceledOnTouchOutside(true);
                        alertDialog2.show();
                        break;
                    case(SETUPRECEIVED):
                        statusText.setText("Find all enemy ships!");
                        gamePhase(mGrid, monitor);
                        break;
//                    }
                }

            }

        };

        server = new ServerThread(monitor, mGrid, handler);
        server.start();

        statusText.setText("Click squares to position your ships!");

        mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener(mGrid));

        readyButton.setEnabled(false);
        readyButton.setVisibility(View.VISIBLE);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusText.setText("Awaiting opponent..");
                mGrid.setVisibility(View.GONE);
                monitor.addMyPositions(getMyPositions());
                monitor.setupPhase = false;
                readyButton.setVisibility(View.GONE);
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
