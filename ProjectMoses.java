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

import javax.imageio.ImageIO;
import org.opencv.core.Mat; 
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;



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
	
	
	// Create the window and start the Thread
	public ProjectMoses() throws FileNotFoundException
	{ 
		addWindowListener(new ProjectMosesWindow(this));
		t=new Thread(this);
		t.start();
		flag=false;	
	}
	
	
	
    // Start the class thread and window
	public static void main(String s[]) throws IOException
	{ 
		ProjectMoses b=new ProjectMoses();	// Create Window
	  b.setSize(new Dimension(1000,1000));		// Window Dimensions
	  b.setTitle("Project Moses");			// Set Title of window
	  b.setVisible(true);
	  
	  playerImage = ImageIO.read(new File(playerImageFile)); // Create Player Image

	}
	
	// This Thread is used to run everything
	public void run()
	{
		System.loadLibrary("opencv_java248"); // Load Native Library version 248	
		CascadeClassifier faceDetector = new CascadeClassifier("./src/haarcascade_frontalface_alt_tree.xml");	// Classifier for the Face Detection
		Mat webcam_image = new Mat(); 
		VideoCapture capture = new VideoCapture(0); 
		
		// Capturing and face detection
		// Can detect more than one face
		if( capture.isOpened()) 
		{ 
			// Create Game Variables
			Player player = new Player((int)xCoor, playerYcoor, playerSize, playerSize);;
			
			for(int i=0;i<5; i++)
			{
				enemies.add(new Enemy("LOL", i*50, i*100));
			}
			
			
			while( true ) 
			{ 
				updateFace(faceDetector, webcam_image, capture);
				for(Enemy enemy: enemies)
				{
					enemy.updateCoor();;
				}
				
				// Enter Stuff Here
				
				
				
				
				
				
				
				removeAll();
				repaint();	// Call Paint after Values have been set
			} 
		} 
	}
	
	  public void paint(Graphics g)
	  { 
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
	
	
	
	public static void updateFace(CascadeClassifier cascade, Mat image, VideoCapture cap)
	{
		cap.read(image); 
		
		if( !image.empty() ) 
		{ 
			MatOfRect faceDetections = new MatOfRect();
			cascade.detectMultiScale(image, faceDetections);
						
			xCoor = 0;
			yCoor = 0;
		
			// Draw a bounding box around each face.
			for (Rect rect : faceDetections.toArray()) 
			{
				xCoor = -(((double)rect.x)/400*1000-100)+950;
				yCoor = ((double)rect.y)/400*1000+500;
				break;
			}
			
			System.out.println(String.format("Detected %s faces: %s, %s", faceDetections.toArray().length, xCoor, yCoor));
		}
	}
	

}
