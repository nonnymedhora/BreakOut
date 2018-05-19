/* breakOutUI.java -- the UI */

import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics2D;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;


public class breakOutUI extends JFrame implements Runnable {
	
	// ctor 
	public breakOutUI() {
		this.run();
		this.initUI();
        this.startNow = true;
    }	
	
	public static final int WIDTH 	= 500;
	public static final int HEIGHT 	= 600;
	int constant = 20;
	
	// fields in the object for UI controls //
   	// bottom most panel - Buttons for all standard operations //
   private  JButton     start;
   private  JButton     pause;
   private  JButton     resume;
   private  JButton 	exit;
   private 	JButton 	reset;
   
   	// panel to hold the buttons //
   private  JPanel      buttonPanel;
   
	// panel to hold the game
   private	JPanel		gamePanel;
   private 	Canvas	myCanvas;
   
   	// panel for time related activity
   private	JPanel		timerPanel;
   	// labels and textfields
   private	JLabel		la_Time;
   private	JTextField	tf_Time;
   private	JLabel		la_BallsLeft;
   private	JTextField	tf_BallsLeft;
   private	JLabel		la_BricksLeft;
   private	JTextField	tf_BricksLeft;
   private 	JLabel		la_Score;
   private	JTextField	tf_Score;
   
   	// status panel
   private	JPanel		statusPanel;   
   /// user customizable settings //
   private	JLabel		la_Level;
   private  Choice	 	ch_Level;		// advanced, novice
   private 	int 		level;
   								// including just in case we wish to add such options later
  // private 	JLabel		la_NumberOfBricks;
  // private	Choice		ch_NumberOfBricks;	// changes the number of bricks the user would like to play with
   
  // private 	JLabel		la_BrickSize;
  // private  Choice		ch_BrickSize;	// changes the size of the bricks  
   
   private	JLabel		la_useMouse;
   private	Choice		ch_useMouse;
   
   private 	JLabel		la_BallSize;
   private	Choice		ch_BallSize; 	// changes the size of the ball
      
   private 	JLabel		la_PaddleSize;
   private	Choice		ch_PaddleSize; 	// changes the size of the paddle
 
   private	JLabel		la_Speed;
   private  Choice		ch_Speed;		// changes the speed of the ball | s
   
	// actual object //
	
    private int 		brickWidth = 2;
    private int 		brickHeight = 1;
    private int 		bricksPerLine;
    private int 		numberOfLines;
    private int 		arraySize;
    
    private int 		ballYstart;
    private int 		ballXstart;
    Color 	color = Color.yellow;
    Color 	background = Color.black;
    Paddle 	paddle;
    private int 		paddleWidth = 120;
    private int 		paddleXmax;
    
    Deflector deflector1;
    
    ball 	ball;
    private int 		ballWidth = 10;
    private int 		ballHeight = 10;
    
    private Image 		offscreenImg;
    private Graphics 	offscreenGraphics;
    private Thread 		thread;
    private int 		z;
    
    private Vector 		v = new Vector();
    Enumeration e;
    private brick 		brickArray[];
    
    private long 		startTime;
    private Date 		date;
    
    private boolean 	startNow;
    private int 		xexit;
    
    private int 		oldVsize;
    
    private Font 		f;
    private FontMetrics fm;
    private int 		fs;
    private int 		xpos;
    private int 		ypos;
    private int 		width;
    private int 		height;
    private int 		xvel;
    private int 		yvel;
    
    private Paddle 		p;
    
    private int velSave;
    
    private MyBoard  theBoard;
    
    boolean	useMouse = true;

	
    
   /**********PRIVATE METHODS ****************/
   /*   Initialize UI controls   */
   private  void  initUI() {
   	
  Container   cp;
      
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      this.setTitle("Bawa BreakOut");

      this.setSize(WIDTH, HEIGHT);      
      this.setResizable(true);

      cp = this.getContentPane();

     // Setup Menus
	// Create toolbar
	JMenuBar menuBar = new JMenuBar();
	setJMenuBar (menuBar);
	
	// Create a menu labeled File, accelerator F
	JMenu file = new JMenu ("File");
	file.setMnemonic (KeyEvent.VK_F);	
	JMenuItem item;
	// Create a menu item Exit, accelerator x
	// Have it call doCloseCommand when selected
	file.add (item = new JMenuItem ("Exit"));
	item.setMnemonic (KeyEvent.VK_X);
	item.addActionListener (new ActionListener() {
	
	public void actionPerformed (ActionEvent e) {
	doExitCommand (0);
	}
	});
	
	// Add file menu to menu bar
	menuBar.add (file);
	
	// Create a menu labeled Help, accelerator H
	JMenu help = new JMenu ("Help");
	help.setMnemonic (KeyEvent.VK_H);

	// Create a menu item About, accelerator A
	// Have it call doAboutCommand when selected
	help.add (item = new JMenuItem ("About"));
	item.setMnemonic (KeyEvent.VK_A);
	item.addActionListener (new ActionListener() {
	public void actionPerformed (ActionEvent e) {
	//doAboutCommand();
	}
	});
	
	// Add help menu to menu bar
	menuBar.add (help);  	
   	
   
   // user customizable settings //
   /// instantiate the panels individually first//
   this.statusPanel		=	new JPanel();
   
   
   /// instantiate components for panels and add resp ///
   /// statusPanel ///
   	  this.la_Level	=	new JLabel(" Change Level ");
      this.ch_Level	=	new Choice();
      		ch_Level.add(" Advanced ");
      		ch_Level.add(" Novice ");
     /* 		// in case we wish to use these options
      this.la_NumberOfBricks	=	new JLabel(" Bricks # ");
      this.ch_NumberOfBricks	=	 new Choice();
      		ch_NumberOfBricks.add(" 40 ");
      		ch_NumberOfBricks.add(" 50 ");
      		ch_NumberOfBricks.add(" 60 ");
      		
	  this.la_BrickSize		=	new JLabel(" Brick Size ");
	  this.ch_BrickSize		=   new Choice();
	  		ch_BrickSize.add(" Small ");
	  		ch_BrickSize.add(" Medium ");
	  		ch_BrickSize.add(" Large ");	  
	  */
    
      this.la_useMouse   		= 	new JLabel(" Controller  ");
      this.ch_useMouse			=	new	Choice();
      		ch_useMouse.add(" Mouse or Keyboard ");
      	    ch_useMouse.add(" Keyboard ");
      	          		
      this.la_PaddleSize		=	new JLabel(" Paddle Size ");
	  this.ch_PaddleSize		=   new Choice();
	  		ch_PaddleSize.add(" Small ");
	  		ch_PaddleSize.add(" Medium ");
	  		ch_PaddleSize.add(" Large ");
	 				
      this.la_BallSize			=  	new JLabel(" Ball Size ");
      this.ch_BallSize			=	new Choice();
      		ch_BallSize.add(" Tiny ");
      		ch_BallSize.add(" Average ");
      		ch_BallSize.add(" Huge ");
      
      this.la_Speed	=	new JLabel(" Speed ");
	  this.ch_Speed	=	new Choice();
	  		ch_Speed.add(" Blitz ");
	  		ch_Speed.add(" Av Jo ");
	  		ch_Speed.add(" Wimp ");
	  		
	  this.ch_Speed.select(" Wimp ");
	  this.ch_BallSize.select(" Tiny ");
	  this.ch_PaddleSize.select(" Large ");
	  this.ch_Level.select(" Novice "); 
	  
	// add items to the statusPanel //
   	this.statusPanel.setLayout(new BoxLayout(this.statusPanel,BoxLayout.Y_AXIS));    	
  	this.statusPanel.add(la_useMouse);
  	this.statusPanel.add(ch_useMouse);
  	
   	this.statusPanel.add( la_Level );
   	this.statusPanel.add( ch_Level );
		// in case we wish these choices
  // 	this.statusPanel.add( la_NumberOfBricks );
  // 	this.statusPanel.add( ch_NumberOfBricks );
  // 	this.statusPanel.add( la_BrickSize );
  // 	this.statusPanel.add( ch_BrickSize );
   	this.statusPanel.add( la_PaddleSize );
   	this.statusPanel.add( ch_PaddleSize );   	
   	this.statusPanel.add( la_BallSize );
   	this.statusPanel.add( ch_BallSize);
   	this.statusPanel.add( la_Speed );
   	this.statusPanel.add( ch_Speed );
   	
   	this.statusPanel.setSize( 540, 100); 	
   	
    this.buttonPanel		=	new JPanel();
   
      this.start	=	new	JButton(" Start ");
      this.pause	=	new JButton(" Pause ");
      
  	  this.resume	=	new JButton(" Resume ");
  	  
      this.exit		=	new JButton(" Exit ");
      this.reset	=	new JButton(" Reset ");
      	resume.setEnabled(false);	
      	pause.setEnabled(false);
      	reset.setEnabled(false);
   
      	// add items to buttonPanel //
   	this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel,BoxLayout.X_AXIS)); 
   	this.buttonPanel.add( start  );
   	this.buttonPanel.add( pause  );
   	this.buttonPanel.add( resume );
   	this.buttonPanel.add( reset );
   	this.buttonPanel.add( exit   );
   
   	this.buttonPanel.setSize( 440, 60 );
   	
    
    this.timerPanel		=	new JPanel();
   	
   	// ADD ITEMS TO THE TIMERPANEL //
   	this.timerPanel.setLayout( new BoxLayout(this.timerPanel,BoxLayout.X_AXIS));   	
   	this.la_Time 	= new JLabel("      Time  ");
   	this.tf_Time	= new JTextField(4);
   		tf_Time.setEditable(false);
   		tf_Time.setHorizontalAlignment(JTextField.CENTER);

   	this.la_BallsLeft	=	new JLabel(" Balls Left ");
   	this.tf_BallsLeft	=	new JTextField(4);
   		tf_BallsLeft.setEditable(false);
   		tf_BallsLeft.setHorizontalAlignment(JTextField.CENTER);
   	
   	this.la_BricksLeft	=	new JLabel(" Bricks Left ");
   	this.tf_BricksLeft	=	new JTextField(4);
   		tf_BricksLeft.setEditable( false );
   		tf_BricksLeft.setHorizontalAlignment(JTextField.CENTER);
   		
   	this.la_Score		=	new JLabel(" Score ");
   	this.tf_Score		=	new JTextField(4);
   		tf_Score.setEditable(false);
   	
   	this.timerPanel.add( la_Time );
   	this.timerPanel.add( tf_Time );
   	
   	this.timerPanel.add( la_BallsLeft );
   	this.timerPanel.add( tf_BallsLeft );
   	
   	this.timerPanel.add( la_BricksLeft );
   	this.timerPanel.add( tf_BricksLeft );
   	
   	this.timerPanel.add( la_Score );	// this is being added here as the timerPanel
   	this.timerPanel.add( tf_Score );	// is essentially recording changes
   	   	
   	this.timerPanel.setSize( 50, 50);	
   	
   	this.gamePanel		=	new JPanel(); 
   	myCanvas = new Canvas();
        myCanvas.setBounds(12, 36, 412, 436);
        myCanvas.setBackground(background); 	
    	
    	this.gamePanel.add(myCanvas); 
    
    this.gamePanel.setSize( 440, 440);	
	for(bricksPerLine = myCanvas.getSize().width / brickWidth; ((brickWidth * bricksPerLine + bricksPerLine) - brickWidth) + 1 > myCanvas.getSize().width ; bricksPerLine--);
        offscreenImg = createImage(myCanvas.getSize().width, myCanvas.getSize().height);
  
     	paddle 		= new Paddle(paddleWidth, 5 , 300, 0, 400);  
        ball = new ball(ballXstart, ballYstart, 2, 2, ballWidth, ballHeight, myCanvas, color);	//, background);
	 	deflector1 	= new Deflector( 80, 20, 100, myCanvas);
	 	theBoard = new MyBoard(myCanvas, ball, paddle, null, tf_BallsLeft,tf_BricksLeft, tf_Time, tf_Score, ch_Level);

	 	        
    // ACTION LISTENERS //

    // start button
    this.start.addActionListener(new ActionListener()  {
      	public void actionPerformed(ActionEvent evt) {
      		startAll();
         }
      });
      
    // pause button  
   this.pause.addActionListener(new ActionListener()  {
      	public void actionPerformed(ActionEvent evt) {
           theBoard.setPaused(true);
           pause.setEnabled(false);
           resume.setEnabled(true);
         }
      });
  	// resume button
   this.resume.addActionListener(new ActionListener()  {
      	public void actionPerformed(ActionEvent evt) {
      		try {
           pause.setEnabled(true);
           resume.setEnabled(false);
        	theBoard.setPaused(false);
          } catch(IllegalMonitorStateException ilmse) {System.out.println("ILLEGAL");}
         }
      });
   
  	// reset button
   this.reset.addActionListener(new ActionListener()  {
      	public void actionPerformed(ActionEvent evt) {
      				theBoard.interrupt();
  				ball.setXpos(20);
  				theBoard = new MyBoard(myCanvas, ball, paddle, deflector1, tf_BallsLeft, tf_BricksLeft, tf_Time, tf_Score, ch_Level);
        		startAll();
  		 }
      });
      
  	// exit button
   this.exit.addActionListener(new ActionListener()  {
      	public void actionPerformed(ActionEvent evt) {
           doExitCommand(0);
         }
      });
      
    // choice - use the mouse or keyboard
    this.ch_useMouse.addItemListener(new ItemListener() {
    	public void itemStateChanged(ItemEvent evt) {
    		if(ch_useMouse.getSelectedItem().equals(" Mouse or Keyboard ")) {
				theBoard.setUseMouse(true);  						
    	    } else {
    	    	theBoard.setUseMouse(false);
    	   }
    }
   });
   
  	// choice - number of bricks - in case we wished this
 /* this.ch_NumberOfBricks.addItemListener(new ItemListener()  {
 	 	public void itemStateChanged(ItemEvent evt) {      
  			if (ch_NumberOfBricks.getSelectedItem().equals(" 40 ")){
  				theBoard.setUpBricks(4,10, 30, 20);
  			}
  			else if (ch_NumberOfBricks.getSelectedItem().equals(" 50 ")){
  				theBoard.setUpBricks(5,10, 30, 20);
  			}
  			else if (ch_NumberOfBricks.getSelectedItem().equals(" 60 ")){
  				theBoard.setUpBricks(5,12, 30, 20);
  		}
   	}
  });
  */
  	// choice - ball size
  this.ch_BallSize.addItemListener(new ItemListener()  {
 	 	public void itemStateChanged(ItemEvent evt) {
 			 if (ch_BallSize.getSelectedItem().equals(" Tiny ")) {
  				ballWidth = 10;
  				ballHeight = 10;
  			}
		 	else if (ch_BallSize.getSelectedItem().equals(" Average ")) {
		  		ballWidth = 20;
		  		ballHeight = 20;
		  	}
		  	else if (ch_BallSize.getSelectedItem().equals(" Huge ")) {
		  		ballWidth = 40;
		  		ballHeight = 40;
		 	}
		 	theBoard.getBall().setSize(ballWidth, ballHeight);
		}
   });
		  
	// choice - paddle size
  this.ch_PaddleSize.addItemListener(new ItemListener()  {
 	 	public void itemStateChanged(ItemEvent evt) {
 		  	if (ch_PaddleSize.getSelectedItem().equals(" Small ")) {
		  		theBoard.getPaddle().setWidth(40);
		  	}
		  	else if (ch_PaddleSize.getSelectedItem().equals(" Medium ")) {
		  		theBoard.getPaddle().setWidth(80);
		  	}
		  	else if (ch_PaddleSize.getSelectedItem().equals(" Large ")) {
		  	theBoard.getPaddle().setWidth(120);
		  }
		}
	});
	
	// choice - brick size  -- in case we wished this
	/*
	this.ch_BrickSize.addItemListener(new ItemListener()  {
 	 	public void itemStateChanged(ItemEvent evt) {      
  			if (ch_BrickSize.getSelectedItem().equals(" Small ")){
  				theBoard.setUpBricks(theBoard.numberOfLines, theBoard.bricksPerLine, 30, 20);
  			}
  			else if (ch_NumberOfBricks.getSelectedItem().equals(" Medium ")){
  				theBoard.setUpBricks(theBoard.numberOfLines, theBoard.bricksPerLine, 40, 20);
  			}
  			else if (ch_NumberOfBricks.getSelectedItem().equals(" Large ")){
  				theBoard.setUpBricks(theBoard.numberOfLines, theBoard.bricksPerLine, 50, 20);
  		}
   	}
  });	
	*/
	
	// choice - ball speed
	this.ch_Speed.addItemListener(new ItemListener()  {
 	 	public void itemStateChanged(ItemEvent evt) {      
  			if (ch_Speed.getSelectedItem().equals(" Blitz ")){
  				theBoard.getBall().setVel(5,5);
  			}
  			else if (ch_Speed.getSelectedItem().equals(" Av Jo ")){
  				theBoard.getBall().setVel(3,3);
  			}
  			else if (ch_Speed.getSelectedItem().equals(" Wimp ")){
  				theBoard.getBall().setVel(2,2);
  		}
   	}
  });
  
  	// choice - level
  	this.ch_Level.addItemListener(new ItemListener()  { 
  		public void itemStateChanged(ItemEvent evt) {
  			if(ch_Level.getSelectedItem().equals(" Advanced ")) {
  				level = 2;
	  	  			 theBoard.interrupt();
	 
  				theBoard = new MyBoard(myCanvas, ball, paddle, deflector1,tf_BallsLeft, tf_BricksLeft, tf_Time, tf_Score, ch_Level);			  			
  			}
  			else if (ch_Level.getSelectedItem().equals(" Novice ")) {
  				level = 1;
  					theBoard.interrupt();
	 
  					theBoard = new MyBoard(myCanvas, ball, paddle, null, tf_BallsLeft, tf_BricksLeft, tf_Time, tf_Score, ch_Level);	
 	  			}
  		}
  	});
  			
   	cp.setLayout(new BorderLayout());
   	cp.add( this.buttonPanel,	"South");
   	cp.add( this.statusPanel,	"West");
   	cp.add( this.gamePanel, 	"Center");
   	cp.add( this.timerPanel,	"North");

      this.pack();
      this.setVisible(true);
} // ends initUI   



  // implement Runnable
   public void run () {
   	   }
   
   /** accessors **/
   public int getLevel() {
   	return ( this.level );
   }
  
   /** displayers and UI checks**/   
   public void setColor() {
        color = new Color(	(int)Math.floor(Math.random() * 256D), 
        					(int)Math.floor(Math.random() * 256D), 
        					(int)Math.floor(Math.random() * 256D) );
        Graphics g = getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        setBackground(color);
    }

  
    	
	public void startAll() {
       try {
            startNow = true;
           // setTime();
           pause.setEnabled(true);
	            resume.setEnabled(false);
            reset.setEnabled(true);
			theBoard.start();
		} catch (IllegalThreadStateException iltse) {};
    }
    
    public void pause(int time) {
        try {
           Thread.sleep(time);
           //setTime();
            return;
        }
        catch(InterruptedException _ex) {
            return;
        }
   }
  
	public void doExitCommand (int status) {
	System.exit (status);
	}

   /** mutators **/
   public int setLevel( int level ) {
   	return this.level	=	level;
   }
  
   
   public static void main(String args[]) throws Exception {
	breakOutUI bO	=	new breakOutUI();
	}

}