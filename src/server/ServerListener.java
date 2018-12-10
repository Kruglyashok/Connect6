package server;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListener extends Thread{
    ServerSocket ss;
    int port;
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;   
    public static ArrayList<ServerThread> clients = new ArrayList();
    ServerThread st;
    Color sendColor = Color.WHITE;
    public ServerListener(int port, InetAddress ip) {
        try {
            ss = new ServerSocket(port, 0 ,ip);
        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        int myInd =0;
        while(!ss.isClosed() && clients.size() < 2) {
            try {
                cs = ss.accept();
                st = new ServerThread(cs, myInd);
                st.start();
                clients.add(st);
                st.sendColor(myInd);
                myInd++;
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }                
    }
}
