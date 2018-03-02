package online.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import online.GameThread;
import online.Monitor;

/**
 * Created by otto on 2018-02-18.
 */

public class ServerThread extends GameThread {

    private Monitor m;
    private MulticastSocket ms;
    private InetAddress ia;
    private DatagramSocket socket;
    private String clientAddress;

    public ServerThread(Monitor m){
            this.m = m;
            id = 1;
    }

    public void run(){
        try {
            ms = new MulticastSocket(8080);
            ia = InetAddress.getByName("224.0.50.50");
            ms.joinGroup(ia); //creates a socket at which clients can connect

            byte[] start = new byte[64];
            DatagramPacket init = new DatagramPacket(start, start.length);
            System.out.println("WAITING FOR PACKET");
            ms.receive(init);
            System.out.println("PACKET RECEIVED");
            clientAddress = init.getAddress().getHostAddress(); //extracts the ip address from the client
            System.out.println("Client @ " + clientAddress.toString());
            ms.close();

            socket = new DatagramSocket(8080); //creates a socket where the client and server can communicate
            start = "0".getBytes();
            init = new DatagramPacket(start, start.length, InetAddress.getByName(clientAddress), 8080); //sends the package to the correct address and socket
            socket.send(init);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(m.setupPhase){
            }

        System.out.println("SETUP PHASE END");

        try {
            byte[] data = m.getSetupPositions().getBytes();
            DatagramPacket myPositions = new DatagramPacket(data, data.length, InetAddress.getByName(clientAddress), 8080);
            socket.send(myPositions);
        } catch (IOException e) {
            e.printStackTrace();
        }



        while(!this.isInterrupted()){
            try{
                byte[] data = new byte[8];
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                socket.receive(receivePacket);
                System.out.println("Got shot on square: " + new String(receivePacket.getData()));

//                int target = m.waitTurn();
//                data = {(byte) target};
//                DatagramPacket sendPacket = new DatagramPacket(data, data.length,  InetAddress.getByName(clientAddress), 8080);
//                socket.send(sendPacket);
//                System.out.println("Shooting square: " + target);


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void killSockets() {
        if (ms != null) {
            ms.close();
        }
        if (socket != null){
            socket.close();
        }
    }
}
