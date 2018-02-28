package Online.Client;

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

        } catch (IOException e) {
            System.out.println("IO error " + e);
        }
    }

    public void run(){
        try {
            ds = new DatagramSocket(8080); //create a new socket where the client and server can communicate
            byte[] buffer = (ia.toString()).getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(broadCastHost), 8080);
            ds.send(packet); //send the client ip to the multicast address (server)
            byte[] recvBuf = new byte[1024];
            DatagramPacket recv = new DatagramPacket(recvBuf, recvBuf.length);
            ds.receive(recv);
            destHost = recv.getAddress(); //save the server address

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!this.interrupted()) {
            Message msg = Message.obtain();
            try {
                //send stuff
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
