import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;



String testies = "";
//-------- put this in main

EventQueue.invokeLater(new Runnable()
         {
             
            @Override
             public void run()
             {
                 
                 new test();         
             }
         });
         
/////---------------------------------------



public InputReader()//would be name of the class
    {
    inputText.addKeyListener(new KeyListener()
        {
              @Override 
              public void keyPressed(KeyEvent e)
              {
            	  char var = e.getKeyChar();
                  String svar = var + "";
                  //String enter = "\n";
                  
                  if(var == KeyEvent.VK_ENTER)//if the key is ENTER
                	  //feedbackText.append("the enter key has been pressed \n");
                  
                  testies = testies.concat(svar);
                  
                  
                  
            	  //feedbackText.append("Lets try if this worked!" + testies + "\n");
            	  //feedbackText.append("Key Pressed: " + e.getKeyChar() + "\n");
               
                  
              }
              @Override
              public void keyReleased(KeyEvent e)
              {
                  //feedbackText.append("Key Released: " + e.getKeyChar() + "\n");
              }
              
              @Override
              public void keyTyped(KeyEvent e)
              {
                  //The getKeyModifiers method is a handy
                  //way to get a String representing the
                  //modifier key.
                 //feedbackText.append("Key Typed: " + e.getKeyChar() + " " + KeyEvent.getKeyModifiersText(e.getModifiers()) + "\n");
              }
        });
