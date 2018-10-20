/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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

/**
 *
 * @author One
 */
public class ServerThread extends Thread{
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Point myStep1, myStep2;
    Color myColor;
    
    public ServerThread(Socket cs) {
        myStep1 = new Point();
        myStep2 = new Point();
        try {
            this.cs = cs;
            dis = new DataInputStream(cs.getInputStream());
            dos = new DataOutputStream(cs.getOutputStream());
            ois = new ObjectInputStream(cs.getInputStream());
            oos = new ObjectOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sendColors() {
    
    }
    @Override 
    public void run() {
       /*
        new Thread() {
        @Override 
            public void run() {
                while(true) {
                    try{
                        myStep1 = (Point) ois.readObject();
                        myStep2 = (Point) ois.readObject();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
        new Thread() {
        @Override
            public void run() {
                    while(true) {
                        try {
                            if (myStep1 != null && myStep2 != null) {
                                System.out.append(myStep1.x + " " + myStep2.x + " ");
                            }
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
        }.start();
        */
    }    
}
