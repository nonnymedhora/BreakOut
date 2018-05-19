import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;

public class brick
{
// fields
	int xpos;
    int ypos;
    int width;
    int height;
    int lasthit = 1;
    Color color;
    int active; 
    int yStart;
    
    /* ctor */
    public brick (	int xpos, int ypos, int width, int height, Color color, int yStart) {        
        this.xpos = xpos;
        this.ypos = ypos;
        this.width = width;
        this.height = height;
        this.color = color;
        this.yStart = yStart;
        this.active = 1;
    }

  
    void setColor(Color color) {
        this.color = color;
    }

    public boolean collision(int ballxpos, int ballypos, int ballwidth, int ballheight) {
    	
    	if ((active != 0) &&  (ballypos < ypos + height) && (ballypos + ballheight > ypos)
       			&& (ballxpos < xpos + width) && (ballxpos + ballwidth > xpos)) {   		
  		active = 0;
  		return(true);
  	} 
  	   	return(false);    
    }
    
    public void paintComponent(Graphics g)  {
    	Graphics2D g2 = (Graphics2D)g;
        
        if (active != 0) {
        g2.setColor(color);
        g2.fillRect(xpos, ypos, width, height);
       }
    }
}