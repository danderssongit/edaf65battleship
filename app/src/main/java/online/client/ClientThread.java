package online.client;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayout;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import online.Monitor;


/**
 * Created by otto on 2018-02-26.
 */

public class ClientThread extends Thread {
    //    private final int ID = 1;
    private Handler handler;
    private Monitor monitor;
    private GridLayout mGrid;
    private InetAddress ia;
    private DatagramSocket socket;
    private String host;
    private InetAddress destHost;
    private String broadCastHost = "224.0.50.50";

    private static final int VICTORY = 1;
    private static final int DEFEAT = 2;
    private static final int SETUPRECEIVED = 3;

    public ClientThread(Monitor monitor, GridLayout mGrid, Handler handler) {
        this.handler = handler;
        this.monitor = monitor;
        this.mGrid = mGrid;
        boolean done = false;
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces(); //returns a list with all interfaces
            while (e.hasMoreElements() && !done) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses(); //returns a list with all inet-addresses which are associated with the interface
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i instanceof Inet4Address && !i.isLoopbackAddress()) {
                        host = i.getHostAddress(); //returns the inet-address for the client
                        done = true;
                        break;
                    }
                }
            }
            ia = InetAddress.getByName(host);
            System.out.println("My Address: " + ia.toString());

        } catch (IOException e) {
            System.out.println("IO error " + e);
        }
    }

    public void run() {
        byte[] data = new byte[64];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        try {
            socket = new DatagramSocket(8080); //create a new socket where the client and server can communicate
            byte[] buffer = (ia.toString()).getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(broadCastHost), 8080);
            System.out.println("SENDING PACKET 1");
            socket.send(packet); //send the client ip to the multicast address (server)
            System.out.println("WAITING FOR PACKET 1");
            if (socket == null) return;
            socket.receive(receivePacket);
            System.out.println("PACKET RECEIVED 1");
            destHost = receivePacket.getAddress(); //save the server address
            System.out.println("HELLO: " + destHost.toString() + "! Waiting for setup.");


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (monitor.setupPhase) {
        }

        try {
            // Sending my ship positions
            data = monitor.getSetupPositions().getBytes();
            DatagramPacket myPositions = new DatagramPacket(data, data.length, destHost, 8080);
            if (socket == null) {
                return;
            }
            socket.send(myPositions);

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
            data = monitor.getScore().getBytes();
            DatagramPacket myPositions = new DatagramPacket(data, data.length, destHost, 8080);
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

        while (!this.isInterrupted()) {

        }

        return;
    }

    public void killSockets() {
        if (socket != null) {
//            socket.disconnect();
            socket.close();
        }
    }
}
