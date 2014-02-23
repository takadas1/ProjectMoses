



// Class for the Enemy Words

public class Enemy
{
	String string;
	int words, xCoor=0, yCoor=0, width=100, height=50;
	int xBlock=0, yBlock=0, bHeight=10, bWidth=10;
	
	int upX = 0, upY = 8;
	
	public Enemy(String s, int x, int y)
	{
		string = s;
		words = s.length();
		xCoor = x;
		yCoor = y;
		
		this.updateBlock();
	}
	
	private void updateBlock()
	{
		xBlock = xCoor;
		yBlock = yCoor-70;
		bHeight = 100;
		bWidth = 30*words;
	}
	
	public void updateCoor()
	{
		xCoor = xCoor+upX;
		yCoor = yCoor+upY;
		xBlock = xBlock+upX;
		yBlock = yBlock+upY;
	}
	
	
}
