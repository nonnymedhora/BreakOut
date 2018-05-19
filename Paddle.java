import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Dimension;

import java.util.*;
public class Paddle { 
    private int 	xpos;
     int 	ypos;
    private int 	width;
     int 	height;
    private Color 	color;
    private	int 	xmax;
    private int 	xmin;
	private	int		ymin;
	private int 	ymax;
	
	public Paddle(Paddle p) {
		this.ypos 		= p.getYpos();
        this.width 		= p.getWidth();
        this.height 	= p.getHeight();
        this.color		= Color.green;
        this.xmin 		= p.getXmin();
        this.xmax		= p.getXmax(); 
   		this.ymin 		= ypos;
   		this.ymax 		= ypos - 40;
    }  
     
    public Paddle(int width, int height, int ypos, int xmin, int xmax) {
        this.ypos 		= ypos;
        this.width 		= width;
        this.height 	= height;
        this.color		= Color.green;
        this.xmin 		= xmin;
        this.xmax		= xmax;
    }

    public void setColor() {
        this.color = new Color(	(int)Math.floor(Math.random() * 250D), 
        				  		(int)Math.floor(Math.random() * 250D), 
        				  		(int)Math.floor(Math.random() * 250D)  );
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
    
    public int collision (int ballxpos, int ballypos, int ballwidth, int ballheight) {
		if(	(ballxpos + ballwidth 	< xpos || ballxpos > xpos + width) ||
           	(ballypos + ballheight 	< ypos || ballypos > ypos + height)	)
           return(-1);
         else {
          	return(ballxpos - xpos);
       	  }        
    }

    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	
        g2.setColor(color);
        g2.fillRect(xpos, ypos, width, height);
    }
    
    public void setYpos(int y) {
    	if (y > ymin) {
    		this.ypos = ymin;		
    	} else if (y < ymax) {
    		this.ypos = ymax;		
    	} else {
    		this.ypos = y;	
    	}	    	
	}
	
    public void setXpos(int x) {
    	if (x < xmin) {
    		this.xpos = xmin;		
        } else if (x > xmax - width) {
     		this.xpos = xmax - width + 10;   	
        } else { 
    	    this.xpos = x;
  		}
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getXpos() {
        return this.xpos;
    }

    public int getYpos() {
        return this.ypos;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getXmax() {
        return this.xmax;
    }

    public int getXmin() {
        return this.xmin;
    }

}