import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage; 
import java.awt.Graphics2D;
import java.io.*;
import java.util.*;
import java.applet.*;
import java.applet.AudioClip;
import javax.swing.JOptionPane;


public class MyBoard extends Thread implements Runnable, KeyListener, MouseMotionListener{  

  int constant = 20;		// paddle moves by this amount
  int level = 1;			
  
  private brick 	brickArray[];
  private Canvas	box;
  Paddle			thePaddle;	
  Deflector			theDeflector1;
  ball				b;
  
  private long		startTime;
  private int 		seconds;
  private int 		minutes;
    
  private Color		background;
  boolean gameOver 		= false;
  boolean startNow      = false;
  int     ballsLeft 	= 3;
  int 	  numberOfLines = 4;
  int     bricksPerLine = 10;
  int 	  brickWidth	= 30;
  int     brickHeight   = 20;
  int	  bricksLeft    = 40;
  int 	  score			= 0;
  
  private boolean		paused;
  private Canvas 		offscreen;
  private BufferedImage bImage = null; 
  private Graphics2D 	gbuffer = null;
  private boolean		useMouse = true;
  private JTextField	tf_BallsLeft;
  private JTextField	tf_BricksLeft;
  private JTextField	tf_Time;
  private JTextField	tf_Score;
  
  private Choice			ch_Level;
  
	// ctor	    
  public MyBoard(Canvas theCanvas, ball b, Paddle thePaddle, Deflector theDeflector1, JTextField tf_BallsLeft, JTextField tf_BricksLeft, JTextField tf_Time, JTextField tf_Score, Choice ch_Level) {
   		setDaemon(true);
   		this.box 		= theCanvas;
 		this.gameOver 	= false;
 		this.background = Color.yellow;
 		this.b 			= b;
 		this.thePaddle 	= new Paddle(thePaddle);
 		this.theDeflector1	=	theDeflector1;
 		this.setUpBricks(numberOfLines, bricksPerLine, brickWidth, brickHeight);
 		this.level = 2;
 		this.bImage = new BufferedImage(theCanvas.getSize().width,
                      			        theCanvas.getSize().height, 
                               			BufferedImage.TYPE_INT_RGB); 
    	
    	Graphics2D gbuffer = bImage.createGraphics();   
		theCanvas.addKeyListener(this);
		theCanvas.addMouseMotionListener(this);
		theCanvas.isFocusable();
		theCanvas.requestFocus();
	  	
	  	Image cursorImage = Toolkit.getDefaultToolkit().createImage( "NoCursor.gif" );        Point cursorPoint = new Point( 0,0 );      
	  
	 	Cursor NoCursor = Toolkit.getDefaultToolkit().createCustomCursor( cursorImage, cursorPoint, "NoCursor" );    
	 	box.setCursor( NoCursor );
	 	this.tf_BallsLeft = tf_BallsLeft;
		
		tf_BallsLeft.setText(Integer.toString(ballsLeft));	
  		this.tf_BricksLeft = tf_BricksLeft;
  		tf_BricksLeft.setText(Integer.toString(bricksLeft));
  		this.tf_Time = tf_Time;
  		startTime = new Date().getTime();
  		tf_Time.setText(setTime());
  		this.tf_Score = tf_Score;
  		tf_Score.setText(Integer.toString(score));
  		this.ch_Level = ch_Level;
  		
   }
    
   public void pause(int time) {
        try {
            Thread.sleep(time);
            return;
        }
        catch(InterruptedException _ex) {
            return;
        }
    }  
    
   
   public void run() {	
 	   ch_Level.setEnabled(false);
 	   startNow = true;  	
        try {
            while(!gameOver) {
            	if (!paused) {
            	 	if (b.moveBall()) {
           		 		ballsLeft -= 1;
           		 		tf_BallsLeft.setText(Integer.toString(ballsLeft));
            			if (ballsLeft < 0) {
            				gameOver = true;
            				ch_Level.setEnabled(true);
     						box.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            			}	
            }
                try {
            		theDeflector1.moveDeflector();
               } catch (NullPointerException e) {}; 
                paintComponent();
                		tf_Time.setText(setTime());
 		               Thread.sleep(1L);
            }
       	 }
        } catch(InterruptedException interruptedexception) { }
    }
    
     
   public void paintComponent() {    	
    	Graphics g = box.getGraphics();
    	Graphics2D g2 = (Graphics2D) g;
     	
        Dimension d = box.getSize();
        gbuffer = bImage.createGraphics();
        
        gbuffer.setColor(background);
     	gbuffer.fillRect(0,0,d.width,d.height);	// game playing rect   
    	int i = 0;
		boolean brickCollision 		= false;
		boolean paddleCollision 	= false;
		boolean deflectorCollision 	= false;
		int paddleXCollision = 0;
		int paddleYCollision = 0;
		boolean theCollision;

		if (gameOver) {
			gbuffer.setColor(Color.black);
			gbuffer.setFont(new Font("Times New Roman", Font.PLAIN, 64));
			gbuffer.drawString("Game Over",	50, 200);
			tf_BallsLeft.setText(" ");
			this.interrupt();
		}	
		while(brickArray[i] != null) {
				if (!brickCollision) {
				  theCollision = brickArray[i].collision(b.getXpos(), b.getYpos(), b.getSize(), b.getSize());
				  if (theCollision) {
						score -= 5;
						tf_Score.setText(Integer.toString(score));
						
						brickCollision = true;
				       	b.setYvel(b.getYvel() * -1);
				    	bricksLeft -= 1;
				    	tf_BricksLeft.setText(Integer.toString(bricksLeft));
				    	if (bricksLeft == 0) {
				    		gameOver = true;
				    	}				    	
				  }     		
			    }
				brickArray[i].paintComponent(gbuffer);		
			i++;				
		}	
		
	 	paddleXCollision = thePaddle.collision(b.getXpos(), b.getYpos(), b.getSize(), b.getSize());
		if(paddleXCollision != -1) {
			if(  b.getYpos() > thePaddle.height + thePaddle.ypos) {	
				score = score;		// no change in score if ball hits paddle from below
			}
			else  
			score += 10;
			tf_Score.setText(Integer.toString(score));
			
			paddleCollision = true;
			b.setXvel((paddleXCollision - (thePaddle.getWidth() / 2)) / 10);	
			b.setYvel( b.getYvel() *  -1)  ;
		}
		
		try {
		 if(theDeflector1.collision(b.getXpos(), b.getYpos(), b.getSize(), b.getSize())) {
			deflectorCollision = true;
			b.setXvel( b.getXvel() + 1);
			b.setYvel( b.getYvel() + 1);
		   }	
		 } catch (NullPointerException e) {}; 
            
		gbuffer.setColor(Color.red);
		b.setColor(Color.red);
		b.setPos(b.getXpos(), b.getYpos());
		b.setSize(b.getSize(),b.getSize());
		b.paintComponent(gbuffer);
		
		thePaddle.paintComponent(gbuffer);
		
		try {
			theDeflector1.paintComponent(gbuffer);
		} catch (NullPointerException e) {}; 
            
		g2.drawImage( bImage,null, null);		
    }
   
    public void setUpBricks(int numberOfLines, int bricksPerLine, int brickWidth, int brickHeight) {
        int z = 0;        
        int yincr = 4;
        int xstart = 4;
        int ystart = 2; 
        int xcnt = 4;
        int ycnt = 350;
        this.numberOfLines = numberOfLines;
        this.bricksPerLine = bricksPerLine;      
        this.brickWidth = brickWidth;	
        this.brickHeight = brickHeight;	
     	 int xincr = (box.getSize().width / bricksPerLine) - brickWidth;        
        Color color;
        
        brickArray = new brick[numberOfLines * bricksPerLine + 10];     
        int r = (int)Math.floor(Math.random() * 256D);
        int g = (int)Math.floor(Math.random() * 84D);
        int b = (int)Math.floor(Math.random() * 256D);
        for(int i = 0; i < numberOfLines; i++) {
            color = new Color(r, g, b);
            for(int x = 0; x < bricksPerLine; x++) {
                brickArray[z] = new brick(xcnt, ycnt, brickWidth, brickHeight, color, ystart);	
                xcnt += xincr + brickWidth;
          		z++;
            }
            ycnt += yincr + brickHeight;
            xcnt = xstart;            
        }
    }
    
    public void keyReleased(KeyEvent event) {
 	 }
 
    public void keyTyped(KeyEvent event) {
	 }
    
    public void keyPressed(KeyEvent event) {
    	
      int keyCode = event.getKeyCode();
      
      if( keyCode == KeyEvent.VK_LEFT ) { 
        this.thePaddle.setXpos (thePaddle.getXpos() - constant);
      }
      else if( keyCode == KeyEvent.VK_RIGHT) {
      	this.thePaddle.setXpos (thePaddle.getXpos() + constant);      
     }
   }  
   
     public void mouseMoved(MouseEvent e) {
		if (useMouse) {
		//	this.thePaddle.setYpos(e.getY());  // in case we want the paddle to move in x-y space
	  		if (e.getX() < 0) {
	  			this.thePaddle.setXpos(0);
  	  	
	  		} else if (e.getX() > box.getSize().width) {
	  			this.thePaddle.setXpos(box.getSize().width);
	  		} else {
	 	 		this.thePaddle.setXpos(e.getX());
  			}
  	   }
    }

    public void mouseDragged(MouseEvent e) {
    }


	public ball getBall() {
		return(b);	
   } 

	public void setBall(ball b) {
		this.b = b;
   } 
   public Paddle getPaddle() {
   		return (thePaddle);
   	}
   public void setPaddle(Paddle thePaddle) {
   		this.thePaddle = thePaddle;
   	}    	
   	
   	public void setUseMouse(boolean useMouse) {
   		this.useMouse = useMouse;	
   }
   
   public void setPaused(boolean paused) {
   		this.paused = paused;   		
   }   
     
   public String setTime() {
      
        Date date = new Date();
        long thisTime = date.getTime();
        long diffTime = thisTime - startTime;
        long hundOfSec = diffTime / 10L;
        if(hundOfSec > 99L) {
            startTime = thisTime;
            hundOfSec = 0L;
            seconds++;
            score += 2;
            tf_Score.setText(Integer.toString(score));
        }
        if(seconds > 59) {
            seconds = 0;
            minutes++;
			if (minutes == 5) {
				theDeflector1 	= new Deflector( 80, 20, 100, box);	//set to advanced mode
				ch_Level.select(" Advanced ");	
			}	
			if (minutes == 20) {
				gameOver = true;
				gbuffer.setColor(Color.black);
				gbuffer.setFont(new Font("Times New Roman", Font.PLAIN, 44));
				gbuffer.drawString("You Won",	50, 300);
			}
			
        }
        
        String sec = Long.toString(seconds);
        if(seconds <= 9) {
            sec = "0" + sec;
        }
        
        String hund = Long.toString(hundOfSec);
        if(hundOfSec <= 9L) {
            hund = "0" + hund;
        }
        
        String min = Long.toString(minutes);
        if(minutes <= 9) {
            min = "0" + min;
        }
     
        return min + ":" + sec + ":" + hund;
    }
}

  
