package online.server;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayout;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import online.GameThread;
import online.Monitor;

/**
 * Created by otto on 2018-02-18.
 */

public class ServerThread extends GameThread {

    private Monitor monitor;
    private Handler handler;
    private GridLayout mGrid;
    private MulticastSocket ms;
    private InetAddress ia;
    private DatagramSocket socket;
    private String clientAddress;

    private static final int VICTORY = 1;
    private static final int DEFEAT = 2;
    private static final int SETUPRECEIVED = 3;


    public ServerThread(Monitor monitor, GridLayout mGrid, Handler handler) {
        this.handler = handler;
        this.monitor = monitor;
        this.mGrid = mGrid;
        id = 1;
    }

    public void run() {
        byte[] buffer = new byte[64];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length);
        try {
            ms = new MulticastSocket(8080);
            ia = InetAddress.getByName("224.0.50.50");
            ms.joinGroup(ia); //creates a socket at which clients can connect

            DatagramPacket init = new DatagramPacket(buffer, buffer.length);
            System.out.println("WAITING FOR PACKET");
            ms.receive(init);
            System.out.println("PACKET RECEIVED");
            clientAddress = init.getAddress().getHostAddress(); //extracts the ip address from the client
            System.out.println("Client @ " + clientAddress.toString());
            ms.close();

            socket = new DatagramSocket(8080); //creates a socket where the client and server can communicate
            buffer = "hi".getBytes();
            init = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(clientAddress), 8080); //sends the package to the correct address and socket
            socket.send(init);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (monitor.setupPhase) {
        }

        System.out.println("SETUP PHASE END");

        try {
            //Sending my ship positions
            buffer = monitor.getSetupPositions().getBytes();
            sendPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(clientAddress), 8080);
            if (socket == null) {
                return;
            }
            socket.send(sendPacket);

            // Waiting for opponent setup phase
            socket.receive(receivePacket);
            String positions = new String(receivePacket.getData());
            positions = positions.substring(0, positions.indexOf("*"));
            monitor.addEnemyPositions(mGrid, positions);

            Message msg = Message.obtain();
            msg.what = SETUPRECEIVED;
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (!monitor.gameOver) {
        }

        try {
            // Sending my score
            buffer = monitor.getScore().getBytes();
            DatagramPacket myPositions = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(clientAddress), 8080);
            if (socket == null) {
                return;
            }
            socket.send(myPositions);

            // Waiting for opponent setup phase
            socket.receive(receivePacket);
            String score = new String(receivePacket.getData());
            System.out.println("Enemy score: " + Integer.parseInt(score.substring(0, score.indexOf("*"))));
            int points = Integer.parseInt(score.substring(0, score.indexOf("*")));
            monitor.setEnemyScore(points);

            Message msg = Message.obtain();
            msg.arg1 = monitor.getScoreInt();
            msg.arg2 = points;

            if(points < msg.arg1){
                msg.what = DEFEAT;
            } else {
                msg.what = VICTORY;
            }
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void killSockets() {
        if (ms != null) {
            ms.close();
        }
        if (socket != null) {
            socket.close();
        }
    }
}
