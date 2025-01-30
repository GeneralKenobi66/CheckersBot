package design_patterns.checkers;


import java.util.ArrayList;
public class Piece {
	int xpos;
	int ypos;
	int x;
	int y;
	boolean isRed;
	boolean isKing;
	ArrayList<Piece> ps;
	public Piece(int xpos, int ypos, boolean isRed, boolean isKing, ArrayList<Piece> ps)
	{
		this.xpos = xpos;
		this.ypos = ypos;
		x = xpos*64;
		y = ypos*64;
		this.isRed = isRed;
		this.isKing = isKing;
		ps.add(this);
	}
	public void move(int xpos, int ypos)
	{
		this.xpos = xpos;
		this.ypos = ypos;
		x = xpos*64;
		y = ypos*64;
	}
	
	public void kill()
	{
		ps.remove(this);
	}
}