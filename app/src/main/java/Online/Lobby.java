package Online;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import Online.Client.JoinGame;
import Online.Server.HostThread;

/**
 * Created by otto on 2018-02-27.
 */

public class Lobby extends Activity {

    /**
     * Sets the view etc
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){

    }

    /**
     * Makes this device the host
     */
    public void hostGame(){
        Intent host = new Intent(Lobby.this, HostThread.class);
        startActivity(host);
    }

    /**
     * Makes this device join an already existing game
     */
    public void joinGame(){
        Intent join = new Intent(Lobby.this, JoinGame.class);
        startActivity(join);
    }
}
