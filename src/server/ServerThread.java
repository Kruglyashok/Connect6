package server;

import client.SendObject;
import java.awt.Color;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread{
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    int gameStance = -1;
    SendObject mySO;
    public int myInd;
    public ServerThread(Socket cs, int myInd) {
        try {
            this.myInd = myInd;
            this.cs = cs;
            dis = new DataInputStream(cs.getInputStream());
            dos = new DataOutputStream(cs.getOutputStream());
            ois = new ObjectInputStream(cs.getInputStream());
            oos = new ObjectOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sendColor(int myInd) {
        try {
            oos.writeObject(new Integer(myInd + 1));
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override 
    public void run() {
        new Thread() {
        @Override 
            public void run() {
                while(gameStance != 3) {
                    try{
                       mySO = (SendObject)ois.readObject();
                       //Point p = (Point)ois.readObject();
                       
                       System.out.append( "inds  " + myInd + "  " + (myInd + 1) % 2 + "\n");
                       ServerListener.clients.get((myInd + 1) % 2).oos.writeObject(mySO);
                        
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }    
}
