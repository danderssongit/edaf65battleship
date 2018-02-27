package Online.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Handler;

import Online.GameThread;

/**
 * Created by otto on 2018-02-18.
 */

public class ServerThread extends GameThread {

    private Monitor m;
    MulticastSocket ms;
    InetAddress ia;
    DatagramSocket socket;
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

            byte[] start = new byte[1024];
            DatagramPacket init = new DatagramPacket(start, start.length);
            ms.receive(init);
            clientAddress = init.getAddress().getHostAddress(); //extracts the ip address from the client
            start = "hello".getBytes();
            ms.close();
            socket = new DatagramSocket(8080); //creates a socket where the client and server can communicate
            init = new DatagramPacket(start, start.length, InetAddress.getByName(clientAddress), 8080); //sends the package to the correct address and socket
            socket.send(init);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(!this.isInterrupted()){
            try{
                //send stuff
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
