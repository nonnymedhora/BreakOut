/* author NMedhora*/
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import java.lang.Thread;


public class ball extends Thread implements Runnable {
   private int oldx = 0;
   private int oldy = 0;
   private int dx = 3;
   private int dy = 3;

   private Canvas 	box;
   private int 		xpos;
   private int 		ypos;
   private int 		width;
    int 		height;
   private int 		xvel;
   private int 		yvel;
   private Color 	color = Color.yellow;
   private Paddle 	p;  
   private int 		velSave;
  

   public ball(int xpos, int ypos, int xvel, int yvel, int width, int height,  
            	Canvas canvas1,  Color color) {	
        setDaemon (true);
        this.xpos 	= xpos;
        this.ypos 	= ypos;
        this.xvel 	= xvel;
        this.yvel 	= yvel;
        this.width 	= width;
        this.height = height;
 		this.box 	= canvas1;
        this.color	= color;
        velSave 	= Math.abs(xvel);
    }
    
  public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.fillOval(xpos, ypos, width, height);       
    }
 
  public boolean moveBall() {  
      oldx = xpos;
      oldy = ypos;
   
      xpos += xvel;
      ypos += yvel;
      
      Dimension d = box.getSize();
      if (xpos < 0 || xpos > d.width - width) {
      	xvel = -xvel; 
      }
     
      if (ypos < 0) {	// || ypos > d.height - YSIZE) { 
     	yvel = -yvel; 
      }

      if(ypos > d.height + 100) {
			ypos = (int) Math.floor(Math.random() * 100D);
			return(true);
    }
    if (xpos < 0) {
    	xpos = 0;	
	}
	if (xpos + width > d.width) {
		xpos = d.width - width;		
   }
    return(false);
  }

    public void setColor() {
        color = new Color((int)Math.floor(Math.random() * 250D), 
        				  (int)Math.floor(Math.random() * 250D), 
        				  (int)Math.floor(Math.random() * 250D));
    }
 
    public void setXvel(int xvel) {
        this.xvel = xvel;
        velSave = Math.abs(xvel);
    }

    public void setYvel(int yvel) {
        this.yvel = yvel;
    }

    public int getXvel() {
        return this.xvel;
    }
    
    public int getYvel() {
        return this.yvel;
    }
    
    public int getXpos() {
        return this.xpos;
    }

    public void setXpos(int ypos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return this.ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setVel(int xvel, int yvel) {
        this.xvel = xvel;
        this.yvel = yvel;
        velSave = Math.abs(xvel);
    }
    public void setPos(int xpos, int ypos) {
    	this.xpos = xpos;
    	this.ypos = ypos;
    }
    
    public int getSize() {
    	return width;	
    }
    
    public Color getColor() {
    	return 	color;
    }
}
