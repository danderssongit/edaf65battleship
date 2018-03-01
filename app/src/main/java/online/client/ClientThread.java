package online.client;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import online.OnlineActivities;

/**
 * Created by otto on 2018-02-26.
 */

public class ClientThread extends Thread {
    private int id;
    private Handler turnHandler;
    private InetAddress ia;
    private DatagramSocket ds;
    private String host;
    private InetAddress destHost;
    private String broadCastHost = "224.0.50.50";


    public ClientThread(Handler turnHandler) {
        this.turnHandler = turnHandler;
        id = 1;
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
        try {
            ds = new DatagramSocket(8080); //create a new socket where the client and server can communicate
            byte[] buffer = (ia.toString()).getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(broadCastHost), 8080);
            System.out.println("SENDING PACKET 1");
            ds.send(packet); //send the client ip to the multicast address (server)
            byte[] recvBuf = new byte[1024];
            DatagramPacket recv = new DatagramPacket(recvBuf, recvBuf.length);
            System.out.println("WAITING FOR PACKET 1");
            ds.receive(recv);
            System.out.println("PACKET RECEIVED 1");
            destHost = recv.getAddress(); //save the server address
            System.out.println("HELLO: " + destHost.toString());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!this.interrupted()) {
//            Message msg = Message.obtain();
            try {
//                byte[] buf = new byte[1024];
//                DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
//                System.out.println("WAITING FOR PACKET 2");
//                ds.receive(receivePacket);
//                System.out.println("PACKET RECEIVED 2");
//                msg.what = (receivePacket.getData()[0]);
//                turnHandler.sendMessage(msg);

//                int move = OnlineActivities.getData();
                byte[] send = {(byte) 42};
                DatagramPacket sendPacket = new DatagramPacket(send,send.length, destHost, 8080);
                ds.send(sendPacket);
                System.out.println("SENDING PACKET 2");
            } catch (Exception e) {
                System.out.println("IO error " + e);

            }
        }

        return;
    }

    public void killSockets() {
        if (ds != null){
            ds.disconnect();
            ds.close();
        }
    }
}
