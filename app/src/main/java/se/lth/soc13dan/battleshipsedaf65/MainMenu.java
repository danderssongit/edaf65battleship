package se.lth.soc13dan.battleshipsedaf65;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Online.Client.JoinGame;
import Online.Server.HostGame;
import Online.Server.Monitor;

public class MainMenu extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Monitor monitor = new Monitor();

        // Buttons and on click listeners
        Button hostButton = (Button) findViewById(R.id.hostButton);
        Button joinButton = (Button) findViewById(R.id.joinButton);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hostGame(monitor);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGame(monitor);
            }
        });
    }

    public void hostGame(Monitor monitor) {
        intent = new Intent(getApplicationContext(), HostGame.class);
        intent.putExtra("monitor", monitor);
        startActivity(intent);
    }

    public void joinGame(Monitor monitor) {
        intent = new Intent(getApplicationContext(), JoinGame.class);
        intent.putExtra("monitor", monitor);
        startActivity(intent);
    }
}
