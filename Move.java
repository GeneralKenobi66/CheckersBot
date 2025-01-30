package design_patterns.checkers;

public class Move
{
	int fx;
	int fy;
	int tx;
	int ty;
	boolean isCapture;
	public Move(int fy1, int fx1, int ty1, int tx1, boolean capture)
	{
		fy = fy1;
		fx = fx1;
		ty = ty1;
		tx = tx1;
		isCapture = capture;
	}
	
	public String toString()
	{
		return Integer.toString(fy)+","+Integer.toString(fx)+"-"+Integer.toString(ty)+","+Integer.toString(tx);
	}
}
