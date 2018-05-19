
import java.util.*;
import java.awt.*;
import java.applet.*;

public class Deflector extends Thread implements Runnable{
    private int xpos;
    private int ypos;
    private int xvel;
    private int yvel;
    private int velSave;
    private int width;
    private int height;
    private Color color;
    private Canvas box;
    private boolean raised = true;
    
    /** ctor **/
    public Deflector(int width, int height, int ypos, Canvas box) {
    	setDaemon (true);
        this.color = Color.blue;
        this.ypos = ypos;
        this.xpos = 250;
        this.xvel = 4;
        this.yvel = 0;
        this.width = width;
        this.height = height;
        this.box = box;
    }

    public boolean  collision(int ballxpos,int ballypos, int ballwidth, int ballheight) {
        if(	(ballxpos + ballwidth  < xpos || ballxpos > xpos + width) ||
           	(ballypos + ballheight < ypos || ballypos > ypos + height))
           return false;	
         else
           return true;
    }
    
    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;    	
        g2.setColor(color);
        g2.fill3DRect(xpos,ypos,width,height, raised);
    }
      
    public void moveDeflector() {  
      int oldx = xpos;   
      int oldy = ypos;
      
      Dimension d = box.getSize();

	  if (xpos > d.width - width) {
	  	xpos = d.width - width;
	  }
      if (xpos >= (d.width - width) || xpos < 0) { 
		xvel *= -1;
      } 
      xpos += xvel;
   } 
    
    public void setVel(int xvel, int yvel) {
        this.xvel = xvel;
        this.yvel = yvel;
        velSave = Math.abs(xvel);
    }

    public void setXpos(int x) {
         this.xpos = x;
    }
    
    public void setWidth(int width) {
         this.width = width;
    }
    
    public int getXpos() {
         return this.xpos;
    }
    
    public int getWidth() {
         return this.width;
    }
}