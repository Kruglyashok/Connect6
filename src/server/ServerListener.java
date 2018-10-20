/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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

/**
 *
 * @author One
 */
public class ServerListener extends Thread{
    ServerSocket ss;
    int port;
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;   
    ArrayList<ServerThread> clients = new ArrayList();
    ServerThread st;
    
    public ServerListener(int port, InetAddress ip) {
        try {
            ss = new ServerSocket(port, 0 ,ip);
        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        while(!ss.isClosed() && clients.size() < 2) {
            try {
                cs = ss.accept();
                st = new ServerThread(cs);
                st.start();
                clients.add(st);
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
}
