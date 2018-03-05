package se.lth.soc13dan.battleshipsedaf65;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import online.Monitor;
import online.client.JoinGame;
import online.server.HostGame;

public class GameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        String score = getIntent().getStringExtra("score");
        String enemyScore = getIntent().getStringExtra("enemyScore");

        AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
        if (getIntent().getStringExtra("winner").equals("yes")) {
            dialog.setMessage("Congratulations, you won. Your score: " + score + ". Opponent's score: " + enemyScore);
        } else {
            dialog.setMessage("Congratulations, you lost. Your score: " + score + ". Opponent's score: " + enemyScore);
        }
        dialog.setPositiveButton("okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}
