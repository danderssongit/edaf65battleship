package online.server;

import android.os.Handler;
import android.os.Message;

import online.GameThread;
import online.Monitor;

/**
 * Created by otto on 2018-02-21.
 */

public class HostThread extends GameThread {
    private int id;
    private Handler turnHandler;
    private Monitor m;

    public HostThread(Handler turnHandler, Monitor m) {
        this.turnHandler = turnHandler;
        this.m = m;
        id = 0;
    }

    @Override
    public void run() {
        while (true) {
            Message msg = new Message();
            try {
                int move = 1; //change what move it is here
                msg.what = move;
                turnHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
