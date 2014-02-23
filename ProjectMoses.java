import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.opencv.core.Mat; 
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import javax.swing.JTextArea;
import twitter4j.*;
import twitter4j.auth.AccessToken;

public class ProjectMoses extends Frame implements Runnable
{
	// Used to run the thread
	Thread t;
	boolean flag;
	
	// Open CV Variables
	private static final long serialVersionUID = 1L; 
	public static double xCoor=0;
	public static double yCoor=0;
	
	// Game Variables
	static BufferedImage playerImage;	// Player Image
	static BufferedImage bossImage;	//Boss Image
	static BufferedImage explodeImage;	//Explode Image
	static int playerSize = 100;
	static int playerYcoor = 850;
	static String playerImageFile = "./src/spaceship2.gif";
	static Enemy enemy = new Enemy("Shit", 500, 500);
	static LinkedList <Enemy> enemies = new LinkedList<Enemy>();
	static Player player;
	static boolean game = true;
	static int score = 0;
	static boolean boss = false, win = false, lose = false, start = false;
	static String bossImageFile = "./src/boss.jpg";
	static int bossY=-1000;
	static String expImageFile = "./src/explode.jpg";
	static Stack<Enemy> death = new Stack<Enemy>();
	
	// Timer for the Game Variables
	static long start_time;
	
	// Background Variables for looping
	static BufferedImage backgroundImage1; // Background Image1
	static BufferedImage backgroundImage2; // Background Image2
	static String backgroundImageFile = "./src/Space.jpg";
	static int back1=0,back2=0;
	
	 // Input Variables
    JTextArea inputText;
    String testies = "";
    InputReader read;
	static LinkedList<String> list;

    
    
    
	// Create the window and start the Thread
	public ProjectMoses() throws FileNotFoundException
	{ 
		addWindowListener(new ProjectMosesWindow(this));
		t=new Thread(this);
		t.start();
		flag=false;	
	}
	
    // Start the class thread and window
	public static void main(String s[]) throws Exception
	{ 
		ProjectMoses b=new ProjectMoses();	// Create Window
	  b.setSize(new Dimension(1000,1000));		// Window Dimensions
	  b.setTitle("Project Moses");			// Set Title of window
	  b.setVisible(true);
	  
	  playerImage = ImageIO.read(new File(playerImageFile)); // Create Player Image
	  backgroundImage1 = ImageIO.read(new File(backgroundImageFile)); // Create Background Image
	  backgroundImage2 = ImageIO.read(new File(backgroundImageFile)); // Create Background Image
	  bossImage = ImageIO.read(new File(bossImageFile)); // Create Boss Image
	  explodeImage = ImageIO.read(new File(expImageFile)); // Create Explode Image

	}
	
	// This Thread is used to run everything
	public void run()
	{
		System.loadLibrary("opencv_java248"); // Load Native Library version 248	
		CascadeClassifier faceDetector = new CascadeClassifier("./src/haarcascade_frontalface_alt_tree.xml");	// Classifier for the Face Detection
		Mat webcam_image = new Mat(); 
		VideoCapture capture = new VideoCapture(0); 
		
		//INPUT VARIABLES
		read = new InputReader();
		try {
			readTumblr();
		} catch (Exception e) {

		}
		
		
		// Capturing and face detection
		// Can detect more than one face
		if( capture.isOpened()) 
		{ 
			// Create Game Variables
			player = new Player((int)xCoor, playerYcoor, playerSize, playerSize); // Create Player
			
			// Times are used to create enemies
			start_time = System.currentTimeMillis();	// Set start time 
			long timer = start_time;	// Timer for when enemy was last created
			
			// Background Locations
			back1 = -1000;
			back2 = -3000;
			
			// Start Screen here

				
				while(!start)
				{
					updateFace(faceDetector, webcam_image, capture);	// Constantly update the location of the spaceship
					repaint();
					
					if(xCoor >= 450) start = true;					
				}

			
			
			
			// Play background music
			Clip clip = null;
		      try
		      {
		          clip= AudioSystem.getClip();
		          clip.open(AudioSystem.getAudioInputStream(new File("./src/Party Rock Anthem.wav")));
		          clip.start();
		      }
		      catch (Exception exc)
		      {
		          exc.printStackTrace(System.out);
		      }		
			
			Clip clip1 = null;

			
			
			// Game loop here
			while(game) 
			{ 
				updateFace(faceDetector, webcam_image, capture);	// Constantly update the location of the spaceship
				player.xCoor = (int) xCoor;	// Update player's x coordinate too
				
				try
				{
					for(Enemy enemy: enemies)	// Constantly update the location of each of the enemies
					{
						checkForCollision(enemy);	// Check for collision with player
						if(!enemy.alive)
						{
							death.add(enemy);
							
							enemies.remove(enemy); // Check to see if they are alive
							int rand = (int)((Math.random()*3)+1);
							
							switch(rand)	// Play a sound 
							{
								case 1: playWav("./src/boom1.wav");
									break;
								case 2: playWav("./src/boom2.wav");
									break;
								case 3: playWav("./src/boom3.wav");
									break;
							}
							
						}
						enemy.updateCoor();
						
						if(enemy.yBlock > 1200) enemies.remove(enemy); // Check to see if they exceed boundaries
					}
				}catch (Exception ConcurrentModificationException){}	// Continue if this exception occurs
				
				// Create new enemy every 2 seconds
				if(System.currentTimeMillis()-timer>1000 && !boss)
				{
					if(!list.isEmpty() && score < 200) // <-- Change Score Limiter
					{
						createEnemy(list.getFirst());
						list.remove(0);		
					}
					else
						{
							boss = true;
							try {
								clip.stop();
								
							      try
							      {
							          clip1= AudioSystem.getClip();
							          clip1.open(AudioSystem.getAudioInputStream(new File("./src/Boss.wav")));
							          clip1.start();
							      }
							      catch (Exception exc)
							      {
							          exc.printStackTrace(System.out);
							      }	
								bossFunction();
							} catch (Exception e) {
							}
						}
					timer = System.currentTimeMillis();
				}

				if(boss)
				{
					if(bossY < 0) bossY += 20;
					
					// Create new enemy after boss reaches point
					if(System.currentTimeMillis()-timer>500 && bossY >-100)
					{
						if(!list.isEmpty())
						{
							createEnemy(list.getFirst());
							list.remove(0);		
						}
						timer = System.currentTimeMillis();
					}				
				}
				
				if(boss && list.isEmpty()) win = true;
				
				if(boss && win)
				{

						winFunction();
						game = false;
						clip1.stop();
				}

			

								
				checkForInput();
				
				updateBackground();	// Update Background
				repaint();	// Call Paint after Values have been set
				
				if(!player.alive)
				{
					game = false;	// Break out of loop
					lose = true;
				}
			}		
			repaint();	
			
			try {
				postTwitter();
			} catch (TwitterException e) {
			}

		} 
	}
	
		

	public void paint(Graphics g)
	  { 
		  if(!start)
		  {
			  g.drawImage(backgroundImage1, 0, back1, 1000, 2100, this);
			  g.drawImage(playerImage, (int) xCoor, playerYcoor, playerSize, playerSize, this);
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
			  g.setColor(Color.white);
			  g.drawString("Move Player Here ---->", 0, 900);
			  g.drawRect(450, playerYcoor, playerSize, playerSize);
		  }
		  else
		  if(game)
		  {
			  // Background Images
			  g.drawImage(backgroundImage1, 0, back1, 1000, 2100, this);
			  g.drawImage(backgroundImage2, 0, back2, 1000, 2100, this);
			  
			  // Boss Draws
			  if(boss) g.drawImage(bossImage, 0, bossY, 1000, 500, this);
			  
			  // Data for Lives and Scores
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
			  g.setColor(Color.white);
			  g.drawString("LIVES: ", 20, 100);
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 25)); 
			  g.drawString("SCORE: " + Integer.toString(score), 750, 100);
			  
			  // Player draw functions
			  if(player!=null)
			  {
				  for(int i=0; i<player.lives; i++)
				  {
					  g.drawImage(playerImage, 40+40*i, 100, 30, 30, this);
				  }
			  }
			  
			  g.drawImage(playerImage, (int) xCoor, playerYcoor, playerSize, playerSize, this);			  

			  // Draw Enemy
			  for(Enemy enemy: enemies)
			  {
				  g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
				  g.setColor(enemy.color);
				  g.fillRect(enemy.xBlock, enemy.yBlock, enemy.bWidth, enemy.bHeight);
				  g.setColor(Color.black);
				  g.drawString(enemy.string, enemy.xCoor, enemy.yCoor);
			  }
			  
			  //Draw Death
			  while(!death.empty())
			  {
				  Enemy en = death.pop();
				  g.drawImage(explodeImage, en.xBlock, en.yBlock, en.bWidth, en.bHeight, this);	
			  }
			  
		  }
		  else if(lose){
			  g.drawImage(backgroundImage1, 0, 0, 1000, 2100, this);
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 100)); 
			  g.setColor(Color.white);
			  g.drawString("GAME OVER", 100, 400);
			  g.drawString("SCORE: " + Integer.toString(score), 100, 600);
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
			  g.drawString("See Twitter @ProjectMoses for daily scores.", 100, 800);

		  }
		  else if(win)
		  {
			  g.drawImage(backgroundImage1, 0, 0, 1000, 2100, this);
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 100)); 
			  g.setColor(Color.white);
			  g.drawString("YOU WIN!", 100, 400);
			  g.drawString("SCORE: " + Integer.toString(score), 100, 600);
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
			  g.drawString("See Twitter @ProjectMoses for daily scores.", 100, 800);
			  g.drawImage(playerImage, 400, 850, playerSize, playerSize, this);			  
		  }


	  }
	
	
	// Update the x,y coordinates of the face
	public static void updateFace(CascadeClassifier cascade, Mat image, VideoCapture cap)
	{
		cap.read(image); 
		
		if( !image.empty() ) 
		{ 
			MatOfRect faceDetections = new MatOfRect();
			cascade.detectMultiScale(image, faceDetections);
						
			xCoor = 0;
			yCoor = -1000;
		
			// Draw a bounding box around each face.
			for (Rect rect : faceDetections.toArray()) 
			{
				xCoor = -(((double)rect.x)/400*1000-100)+950;
				yCoor = ((double)rect.y)/400*1000+500;
				break;
			}			
		}
	}
	
	// Helper method to create Enemies
	public static void createEnemy(String s)
	{
		enemies.add(new Enemy(s, (int)(Math.random()*1000),-100));
	}
	
	// Method called to move background
	public static void updateBackground()
	{
		back1 = back1+50;
		back2 = back2+50;
		
		if(back1 > 1000) back1 = -3000;
		if(back2 > 1000) back2 = -3000;
	}

	// Play the WAV file given the file directory
	public static void playWav(String fileName)
	  {
	      try
	      {
	          Clip clip = AudioSystem.getClip();
	          clip.open(AudioSystem.getAudioInputStream(new File(fileName)));
	          clip.start();
	      }
	      catch (Exception exc)
	      {
	          exc.printStackTrace(System.out);
	      }
	  }

	// Collision Logic - Rob
	public void checkForCollision(Enemy input)
	{
		if(input.xBlock < (player.xCoor + player.width) &&
		(input.yBlock + input.bHeight) > player.yCoor &&
		(input.xBlock + input.bWidth) > player.xCoor &&
		input.yBlock < (player.yCoor + player.height))
		{
			player.loseHealth();
			input.destroy();	
		}
	}
	
	// Works with Input Reader to check for its input
	public void checkForInput()
	{
		String s = read.getString();
		
		if(s.equals(""))
		{
		}
		else
		{
			for(Enemy e:enemies)
			{
				if((e.string).equals(s))
				{
					score += e.words*5;
					e.alive = false;
				}
			}
		}
	}

	// Reading in data from tumblr
	public static void readTumblr() throws Exception
	{
		String longString = HTTPGet.getPostsByBlog("project-moses.tumblr.com"); //project-moses.tumblr.com
		Scanner scan = new Scanner(longString);
		
		list= new LinkedList<String>();
		while(scan.next() != null)
		{
			list.add(scan.next().toLowerCase());
		}

	}

	// Reading in data from String
	public static void readString(String s) throws Exception
	{
		Scanner scan = new Scanner(s);
		list= new LinkedList<String>();
		while(scan.next() != null)
		{
			list.add(scan.next().toLowerCase());
		}
	}
	
	// Boss Function Fight
	public static void bossFunction() throws Exception
	{
		String str = "Hey, didn't think you'd win that easy did ya? Come at me brahhhhh. string. Let's see how fast you can type. lolol boom boooom Hey, didn't think you'd win that easy did ya? Come at me brahhhhh. lolol boom boooom Ahhhhhhhhh 1+1=window ";
		readString(str); // <-- Change Boss STring?
	}

	// Things to do when you win
	public static void winFunction()
	{
		boss = false;
		playWav("./src/Victory.wav");		
	}
	
	public static void postTwitter() throws TwitterException
	{
	      //Your Twitter App's Consumer Key
        String consumerKey = "FNHYyyHAOIorxwGNWWX9A";
 
        //Your Twitter App's Consumer Secret
        String consumerSecret = "eB3GylcC54kCKwwdtRdUYb3HVJZLVQNGyNFZ01aKTyo";
 
        //Your Twitter Access Token
        String accessToken = "2357658374-U2q4lUn2ty2Q0zpIcXm7Wzve7rOTtUSm04FPlfH";
 
        //Your Twitter Access Token Secret
        String accessTokenSecret = "Tpz9heRnOq3a6L82FFoPoPgcZHLTWj6EyDOG27d0EAstw";
 
        //Instantiate a re-usable and thread-safe factory
        TwitterFactory twitterFactory = new TwitterFactory();
 
        //Instantiate a new Twitter instance
        Twitter twitter = twitterFactory.getInstance();
 
        //setup OAuth Consumer Credentials
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
 
        //setup OAuth Access Token
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
 
        //Instantiate and initialize a new twitter status update
        StatusUpdate statusUpdate = new StatusUpdate("NEW USER HAS REACHED SCORE: " + score + " ON PROJECT MOSES!");
       
        Status status = twitter.updateStatus(statusUpdate);
	}
}

