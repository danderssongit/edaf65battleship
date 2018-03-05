package online.client;

import android.os.Handler;
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
//    private Handler turnHandler;
    private Monitor monitor;
    private GridLayout mGrid;
    private InetAddress ia;
    private DatagramSocket socket;
    private String host;
    private InetAddress destHost;
    private String broadCastHost = "224.0.50.50";


    public ClientThread(Monitor monitor, GridLayout mGrid) {
//        this.turnHandler = turnHandler;
//        id = 1;
        this.monitor = monitor;
        this.mGrid = mGrid;
        boolean done = false;
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces(); //returns a list with all interfaces
            while(e.hasMoreElements() && !done)
            {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses(); //returns a list with all inet-addresses which are associated with the interface
                while (ee.hasMoreElements())
                {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i instanceof Inet4Address && !i.isLoopbackAddress())
                    {
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

    public void run(){
        byte[] data = new byte[64];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        try {
            socket = new DatagramSocket(8080); //create a new socket where the client and server can communicate
            byte[] buffer = (ia.toString()).getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(broadCastHost), 8080);
            System.out.println("SENDING PACKET 1");
            socket.send(packet); //send the client ip to the multicast address (server)
            System.out.println("WAITING FOR PACKET 1");
            socket.receive(receivePacket);
            System.out.println("PACKET RECEIVED 1");
            destHost = receivePacket.getAddress(); //save the server address
            System.out.println("HELLO: " + destHost.toString() + "! Waiting for setup.");



        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(monitor.setupPhase){
        }

        try {
            // Sending my ship positions
            data = monitor.getSetupPositions().getBytes();
            DatagramPacket myPositions = new DatagramPacket(data, data.length, destHost, 8080);
            socket.send(myPositions);

            // Waiting for opponent setup phase
            socket.receive(receivePacket);
            String positions = new String(receivePacket.getData());
            positions = positions.substring(0, positions.indexOf("*"));
            monitor.addEnemyPositions(mGrid, positions);
        } catch (IOException e) {
            e.printStackTrace();
        }




        while (!this.interrupted()) {
//            Message msg = Message.obtain();
            try {
                int target = monitor.waitTurn();
//                byte[] data = {(byte) target};
//                DatagramPacket sendPacket = new DatagramPacket(data, data.length, destHost, 8080);
//                socket.send(sendPacket);
//                System.out.println("Shooting square: " + target);


            } catch (Exception e) {
                System.out.println("IO error " + e);

            }
        }

        return;
    }

    public void killSockets() {
        if (socket != null){
            socket.disconnect();
            socket.close();
        }
    }
}
