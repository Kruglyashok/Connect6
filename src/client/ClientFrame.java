/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author One
 */

public class ClientFrame {
   InetAddress ip = null;
    int port = 9876;
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    
    public ArrayList<GridElem> points;
    private Point fieldSize = new Point();
    private Point myStep1;
    private Point myStep2;
    private boolean myTurn = true;
    protected Color myColor;
    
    public class ClientPanel extends JPanel {
        private Point fieldSize = new Point(); //size of the play grid
 // possible points to make a move
        public ClientPanel() {}
        public ClientPanel(ArrayList<GridElem> points) {
        fieldSize.x = 19;
        fieldSize.y = 19;
          MouseAdapter mouseHandler;
            mouseHandler = new MouseAdapter() {
                @Override 
                public void mouseClicked(MouseEvent e) {
                    if (myTurn) {
                    Point clicked = e.getPoint();
                    for (GridElem elem : points) {
                        if (Math.sqrt(Math.pow(elem.coord.x+elem.pos.x*elem.length.x - clicked.x, 2)
                                + Math.pow(elem.coord.y+elem.pos.y*elem.length.y - clicked.y, 2) ) <= elem.length.x/2) {
                        System.out.append("int our point\n");
                        elem.checked = true;
                        /*
                        if (myStep1 == null) {
                        myStep1 = new Point(elem.pos.x, elem.pos.y);
                        }
                        else {
                        myStep2 = new Point(elem.pos.x, elem.pos.y);
                        new Thread() {
                            @Override 
                            public void run() {
                                try{ 
                        
                             if (myStep1 != null && myStep2 != null) {
                            System.out.append("step not null\n");
                            oos.writeObject(myStep1);
                            oos.reset();
                            oos.writeObject(myStep2);
                            oos.reset(); 
                            }
                            } catch (IOException ex) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            }.start();
                        }*/
                        }
                    }
                repaint();
                    }
                }
            };
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }
        @Override
        public Dimension getPreferredSize() {
        return new Dimension(760,760);
        }
        @Override 
        public void invalidate() {
        super.invalidate();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            //super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
                       
            int width = getWidth();
            int height = getHeight();
            
            int lengthX = width/(fieldSize.x + 1);
            int lengthY = height/(fieldSize.y + 1);
            
            int startPosX = lengthX/2;
            int startPosY = lengthY/2;
            
            g2d.setColor(new Color(16762394));
            g2d.fillRect(0,0,getWidth(), getHeight());
            
            if (points.isEmpty()) {
                System.out.append("create new grid\n");
             for (int i=0; i < fieldSize.x + 1; ++i) {
                 for (int j =0; j < fieldSize.y + 1; ++j) {
                     GridElem elem = new GridElem(i,j, startPosX, startPosY, lengthX, lengthY);
                     points.add(elem);
                    }
                }     
            }
            else {
                for(GridElem elem: points) {
                    elem.update(startPosX, startPosY, lengthX, lengthY);
                }
            }
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
             /*for (int i=0; i < fieldSize.x + 1; ++i) {
                 for (int j =0; j < fieldSize.y + 1; ++j) {
                 g2d.drawLine(startPosX + i*lengthX,
                                   j*lengthY,
                                   startPosX + i*lengthX,
                                   j*lengthY + lengthY);
                 
                 }
             }*/
            for (GridElem elem : points) {
                 elem.drawElem(g2d, myColor);
            }
          g2d.dispose();
        }
    
    }
    private void initConnection() {
        try {
            ip = InetAddress.getLocalHost();
            cs = new Socket(ip, port);
            System.out.append("coonnection\n");
            
            dis = new DataInputStream(cs.getInputStream());
            dos = new DataOutputStream(cs.getOutputStream());
              
            oos = new ObjectOutputStream(cs.getOutputStream());
            ois = new ObjectInputStream(cs.getInputStream());
            System.out.append("obj streams created\n");
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ClientFrame () {
        initConnection();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                fieldSize.x = 19;
                fieldSize.y = 19;
                points = new ArrayList<>(fieldSize.x * fieldSize.y);
                JFrame clientFrame = new JFrame("ClientFrame");
                clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                clientFrame.setLayout(new BorderLayout());
                clientFrame.add(new ClientPanel(points));
                clientFrame.pack();
                clientFrame.setLocationRelativeTo(null);
                clientFrame.setVisible(true);
            }
    
    });     
    }
   
    public static void main(String[] args) {
        new ClientFrame();
    }  
    
}
