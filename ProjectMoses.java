import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.opencv.core.Mat; 
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


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
	static int playerSize = 100;
	static int playerYcoor = 850;
	static String playerImageFile = "./src/SpaceShip.jpg";
	static Enemy enemy = new Enemy("Shit", 500, 500);
	static LinkedList <Enemy> enemies = new LinkedList<Enemy>();
	static Player player;
	static boolean game = true;
	static int score = 0;
	
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
			
			// Play background music
			playWav("./src/Party Rock Anthem.wav");
			
			
			
			
			
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
				if(System.currentTimeMillis()-timer>1000)
				{
					if(!list.isEmpty())
					{
						createEnemy(list.getFirst());
						list.remove(0);		
					}
					timer = System.currentTimeMillis();

				}
				
				checkForInput();
				
		
				
				
				
				
				
				
				
				updateBackground();	// Update Background
				repaint();	// Call Paint after Values have been set
				
				if(!player.alive) game = false;	// Break out of loop

				// DO STUFF HERE AFTER PLAYER LOSES
				
				
			}
		} 
	}
	
	  public void paint(Graphics g)
	  { 
		  
		  if(game)
		  {
			  g.drawImage(backgroundImage1, 0, back1, 1000, 2100, this);
			  g.drawImage(backgroundImage2, 0, back2, 1000, 2100, this);
			  
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
			  g.setColor(Color.white);
			  g.drawString("LIVES: ", 20, 100);
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 25)); 
			  g.drawString("SCORE: " + Integer.toString(score), 750, 100);
			  
			  if(player!=null)
			  {
				  for(int i=0; i<player.lives; i++)
				  {
					  g.drawImage(playerImage, 40+40*i, 100, 30, 30, this);
				  }
			  }
			  
			  g.drawImage(playerImage, (int) xCoor, playerYcoor, playerSize, playerSize, this);
			  g.drawRect((int)xCoor, playerYcoor, playerSize, playerSize);
			  
			  
			  for(Enemy enemy: enemies)
			  {
				  g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
				  g.setColor(Color.gray);
				  g.fillRect(enemy.xBlock, enemy.yBlock, enemy.bWidth, enemy.bHeight);
				  g.setColor(Color.black);
				  g.drawString(enemy.string, enemy.xCoor, enemy.yCoor);
			  }
		  }
		  else {
			  g.drawImage(backgroundImage1, 0, 0, 1000, 2100, this);
			  g.setFont(new Font("TimesRoman", Font.PLAIN, 100)); 
			  g.setColor(Color.white);
			  g.drawString("GAME OVER", 100, 500);
			  g.drawString("SCORE: " + Integer.toString(score), 100, 700);
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

}
