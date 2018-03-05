package se.lth.soc13dan.battleshipsedaf65;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import online.client.JoinGame;
import online.server.HostGame;
import online.Monitor;

public class MainMenu extends AppCompatActivity {
    private Intent intent;
    private Monitor monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);



        // Buttons and on click listeners
        Button hostButton = (Button) findViewById(R.id.hostButton);
        Button joinButton = (Button) findViewById(R.id.joinButton);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                monitor = new Monitor(false);
                hostGame(monitor);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                monitor = new Monitor(true);
                joinGame(monitor);
            }
        });
    }

    public void hostGame(Monitor monitor) {
        intent = new Intent(getApplicationContext(), HostGame.class);
//        intent.putExtra("monitor", monitor);
        startActivity(intent);
    }

    public void joinGame(Monitor monitor) {
        intent = new Intent(getApplicationContext(), JoinGame.class);
//        intent.putExtra("monitor", monitor);
        startActivity(intent);
    }
}
