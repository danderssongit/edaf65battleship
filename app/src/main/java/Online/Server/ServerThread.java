package Online.Server;

import java.util.logging.Handler;

import Online.GameThread;

/**
 * Created by otto on 2018-02-18.
 */

public class ServerThread extends GameThread {

    private int id;
    private Monitor m;

    public ServerThread(Monitor m){
            this.m = m;
            id = 1;
    }



}
