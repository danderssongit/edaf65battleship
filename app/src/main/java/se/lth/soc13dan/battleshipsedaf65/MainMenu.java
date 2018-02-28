package se.lth.soc13dan.battleshipsedaf65;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import online.client.JoinGame;
import online.server.HostGame;

public class MainMenu extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Buttons and on click listeners
        Button hostButton = (Button) findViewById(R.id.hostButton);
        Button joinButton = (Button) findViewById(R.id.joinButton);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hostGame();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGame();
            }
        });
    }

    public void hostGame() {
        intent = new Intent(getApplicationContext(), HostGame.class);
        startActivity(intent);
    }

    public void joinGame() {
        intent = new Intent(getApplicationContext(), JoinGame.class);
        startActivity(intent);
    }
}
