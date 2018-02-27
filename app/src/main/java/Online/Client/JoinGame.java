package Online.Client;


import android.os.Bundle;
import android.os.Handler;

import Online.OnlineActivities;

/**
 * Created by otto on 2018-02-27.
 * Starts the client's thread and changes view when the client joins a game
 */

public class JoinGame extends OnlineActivities {

    private ClientThread client;
    private Handler turnHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler turnHandler = new Handler();
        client = new ClientThread(turnHandler);
        client.start();
    }


    @Override
    public void finish() {
        client.interrupt();
        client.killSockets();
    }

}
