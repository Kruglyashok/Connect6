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

public class ClientFrame {
   InetAddress ip = null;
    int port = 9876;
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    public int gameStance;
    private int counter = 0;
    public ArrayList<GridElem> points;
    private Point fieldSize = new Point();
    public Color myColor;
    public Point[] move = new Point[2];
    public class ClientPanel extends JPanel {
    private Point fieldSize = new Point(); //size of the play grid
    
    private Color myColor;
    boolean isGameOver() {
        int count =0;
        Color c = myColor;
        //vertical check
        for (int i =0; i < fieldSize.x +1; i++) {
            for (int j =0; j < fieldSize.y + 1; j++) {
            if (points.get(j*20+i).checked && points.get(j*20+i).elemColor.getRGB() == c.getRGB()) {
            count++;
            }
            else {
                count = 0;
            }
            if(count == 6) return true;
            }
        }
        //horizontal check
        count = 0;
        for (int i = 0; i < fieldSize.y + 1; i++) {
            for (int j =0; j < fieldSize.x + 1; j++) {
            if (points.get(i*20+j).checked && points.get(i*20+j).elemColor.getRGB() == c.getRGB()) {
            count++;
            }
            else {
                count = 0;
            }
            if(count == 6) return true;
            }
        
        //diag check lower top-left-to-bot-right
        for (int rowStart = 0; rowStart  < fieldSize.x - 5; rowStart++) {
            count = 0;
            for (int row = rowStart, col  = 0; row < fieldSize.x+1 && col < fieldSize.y +1; row++, col++ ) {
                if (points.get(col*20 + row).checked && points.get(col*20 + row).elemColor.getRGB() == c.getRGB()) {
                count++;
                }
                else {
                count = 0;
                }
                if(count == 6) return true;
            }
        }
       
        //diag check upper top-left-to-bottom-right
        for(int colStart = 1; colStart < fieldSize.x - 5; colStart++){
        count = 0;
        int row, col;
        for( row = 0, col = colStart; row < fieldSize.y + 1 && col < fieldSize.x  +1; row++, col++ ){
        if (points.get(col*20 + row).checked && points.get(col*20 + row).elemColor.getRGB() == c.getRGB()) {
            count++;
        }
        else {
          count = 0;
        }
        if(count == 6) return true;
        }
        }
        //diag check upper bot-left-to-top-right
        for (int rowStart = fieldSize.y; rowStart > 5; rowStart--) {
            count = 0; 
            int row, col;
            for (row = rowStart, col = 0; row>0 && col < fieldSize.x+1; row--, col++) {
                if (points.get(col*20+row).checked && points.get(col*20+row).elemColor.getRGB() == c.getRGB()) {
                count++;
                }
                else {
                count = 0;
                }
                if (count == 6) return true;
            }
        }
        //diag check lower bot-left-to-top-right
        for (int colStart = 0; colStart < fieldSize.x - 5; colStart++) {
            count = 0; 
            int row, col;
            for (row = fieldSize.y, col = colStart; row > 0 && col < fieldSize.x + 1; row--, col++) {
                if (points.get(col*20+row).checked && points.get(col*20+row).elemColor.getRGB() == c.getRGB()) {
                count++;
                }
                else {
                count = 0;
                }
                if (count == 6) return true;
            }
        }
        
        }
        return false;
    }
        public ClientPanel(ArrayList<GridElem> points, Color myColor) {
        
        fieldSize.x = 19;
        fieldSize.y = 19;
        this.myColor = myColor;
        MouseAdapter mouseHandler;
            mouseHandler = new MouseAdapter() {
                @Override 
                public void mouseClicked(MouseEvent e) {
                    if (gameStance == 1) {
                    Point clicked = e.getPoint();
                    for (GridElem elem : points) {
                        if (Math.sqrt(Math.pow(elem.coord.x+elem.pos.x*elem.length.x - clicked.x, 2)
                                + Math.pow(elem.coord.y+elem.pos.y*elem.length.y - clicked.y, 2) ) <= elem.length.x/2 && (!elem.checked)) {
                        System.out.append("in our point\n");
                        if(!elem.checked) {
                        elem.checked = true;
                        elem.elemColor = myColor;
                        move[counter] = new Point(elem.pos.x, elem.pos.y);
                        counter++;
                        if(counter==2) {
                        counter =0;
                        if (isGameOver()) {
                            try {
                                gameStance = 3;
                                oos.writeObject(new SendObject(move[0], move[1], 3, myColor));
                                
                            } catch (IOException ex) {
                                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else{
                        gameStance = 2;
                            try {
                                   System.out.append("move 0_ :" + move[0].x + "  " + move[0].y + "\n" );
                                   System.out.append("move 1_ :" + move[1].x + "  " + move[1].y + "\n" );
                                oos.writeObject(new SendObject(move[0], move[1], 1, myColor));
                            } catch (IOException ex) {
                                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        }
                        }
                        }
                    }
                repaint();
                    }
                }
            };
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
            new Thread() {
            @Override
            public void run() {
            while(true) {
                try {
                    SendObject recvObj = (SendObject)ois.readObject();
                    System.out.append("i recved obj\n");
                    gameStance = recvObj.getStance();
                    points.get(recvObj.getStep1().x*20+recvObj.getStep1().y).checked = true;
                    System.out.append("move 0 :" + recvObj.getStep1().x + "  " + recvObj.getStep1().y + "\n" );
                    points.get(recvObj.getStep1().x*20+recvObj.getStep1().y).elemColor = recvObj.getColor();
                    
                    points.get(recvObj.getStep2().x*20+recvObj.getStep2().y).checked = true;
                    points.get(recvObj.getStep2().x*20+recvObj.getStep2().y).elemColor = recvObj.getColor();
                    System.out.append("move 1 :" + recvObj.getStep2().x + "  " + recvObj.getStep2().y + "\n" );
                    System.out.append("my gameStance is " + gameStance + "\n");
                    repaint();
                } catch (IOException ex) {
                    Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            }
            }.start();
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
                     GridElem elem = new GridElem(i,j, startPosX, startPosY, lengthX, lengthY, myColor);
                     points.add(elem);
                    }
                }
            points.get(190).checked = true;
            points.get(190).elemColor = Color.BLACK;
            }
            else {
                for(GridElem elem: points) {
                    elem.update(startPosX, startPosY, lengthX, lengthY);
                }
            }
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            for (GridElem elem : points) {
                 elem.drawElem(g2d);
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
            gameStance = (Integer)ois.readObject();
            if (gameStance == 1) myColor = Color.WHITE;
            else myColor = Color.BLACK; 
            System.out.append("my game Stance   " + gameStance + "\n");
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
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
                clientFrame.add(new ClientPanel(points, myColor));
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
