package design_patterns.checkers;

/*
 * Alex Tabakian 2024
 */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CheckersBotDriver {
	public static ArrayList<Piece> ps = new ArrayList<>();
	public static ArrayList<Move> moves;
	public static Game game = new Game();
	public static Piece selectedPiece;
	public static boolean isRedTurn = true;
	public static void main(String[] args) throws IOException 
	{
		System.out.println("Welcome to Alex's Amazing Checkers Bot!");
		System.out.print("\nThe rules are as follows:");
		System.out.print("\n1) If you can promote your checker piece to the last rank, it will turn into a king that can move and capture forwards and backwards");
		System.out.print("\n2) Capturing the opponent's piece is optional");
		System.out.print("\n3) You are unable to capture multiple pieces on a single turn");
		System.out.print("\n4) The winner is the player who captures all of the opponent's pieces, or if there are no legal moves, the player with the most pieces wins");
		System.out.print("\nGood Luck! I haven't been able to score a win against this bot yet :)");
		
		game.setBoard();
		moves = game.allMoves(true);		

		addPieces(ps);	
		Image imgs[] = new Image[4];

		//Images for the four Checkers pieces
		imgs [0] = ImageIO.read(new File("RED.png"));
		imgs [1] = ImageIO.read(new File("BLACK.png"));
		imgs [2] = ImageIO.read(new File("RED_KING.png"));
		imgs [3] = ImageIO.read(new File("BLACK_KING.png"));
		

		JFrame frame = new JFrame("AT's Amazing Checkers Bot");
		frame.setBounds(10,10,526,551);
		JPanel pn = new JPanel()
		{
			public void paint(Graphics g) {
				boolean white = true;
				
				// Draws the 8x8 Checkerboard
				for (int y = 0; y < 8; y++) {
					for (int x = 0; x < 8; x++) {
						if (white)
							g.setColor(new Color(235, 235, 208)); // Light square
						else
							g.setColor(new Color(60, 72, 120)); // Dark square
						g.fillRect(x * 64, y * 64, 64, 64);
						white = !white;
					}
					white = !white;
				}
				
				// Draws legal moves when a piece is selected
				if (selectedPiece != null) {
					g.setColor(new Color(194, 49, 47)); //Red
					for (Move m : moves) {
						if (m.fx == selectedPiece.xpos && m.fy == selectedPiece.ypos) {
							g.fillRect(m.tx * 64, m.ty * 64, 64, 64);
						}
					}
				}
				
				for (Piece p : ps) {
					int ind = 0;
					if (p.isRed && !p.isKing)
						ind = 0;
					if (!p.isRed && !p.isKing)
						ind = 1;
					if (p.isRed && p.isKing)
						ind = 2;
					if (!p.isRed && p.isKing)
						ind = 3;
					g.drawImage(imgs[ind], p.x, p.y, this);
				}
			}
		};
		frame.add(pn);
		frame.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if(selectedPiece!=null)
				{
					if(selectedPiece.isRed == isRedTurn)
					{
						//Centers piece to mouse
						selectedPiece.x = e.getX()-32;
						selectedPiece.y = e.getY()-32;
						frame.repaint();
						
					}
				}				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
		});
		frame.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectedPiece = getPiece(e.getX(),e.getY()-32);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(selectedPiece != null) {
				ArrayList<Move> tempMoves = null;
				boolean isValid = false;
				//Position of captured piece
				int[] jumpPos = {0,0};
				Move selectedMove = null;
				for(Move m : moves)
				{	if(selectedPiece!=null) {
						if(selectedPiece.xpos == m.fx && selectedPiece.ypos == m.fy && 
						e.getX()/64 == m.tx && e.getY()/64 == m.ty)
						{				
							selectedMove = m;
							isValid = true;							
						}
					}
				}
				if(isValid)
				{
					selectedPiece.move(e.getX()/64, e.getY()/64);
					if(!selectedMove.isCapture)
					{
						game.doMove(isRedTurn, selectedMove.fy, selectedMove.fx, selectedMove.ty, selectedMove.tx);
						completedMove();
					}
					else
					{
						jumpPos = game.doJump(isRedTurn, selectedMove.fy, selectedMove.fx, selectedMove.ty, selectedMove.tx);
						completedMove(); 
					}
				}	
				else
				{
					selectedPiece.x = selectedPiece.xpos*64;
					selectedPiece.y = selectedPiece.ypos*64;
				}
				
				if (jumpPos[0] != 0 && jumpPos[1] != 0) {
					Iterator<Piece> iterator = ps.iterator();
					Piece capturedPiece = null;
				
					while (iterator.hasNext()) {
						Piece p = iterator.next();
				
						//Selects captured piece to be removed from list
						if (p.ypos == jumpPos[0] && p.xpos == jumpPos[1]) {
							capturedPiece = p;
						}
					}				
					// Removes piece from list
					if (capturedPiece != null) {
						ps.remove(capturedPiece);
					}			
					// Reset jump position after removal
					jumpPos[0] = 0;
					jumpPos[1] = 0;
		
					frame.repaint();
				}
				frame.repaint(); 
			}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		
		});
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}

	public static void completedMove()
	{		
		isRedTurn = !isRedTurn;
		moves = game.allMoves(isRedTurn);
		if(moves.isEmpty()) {
			GameOver();
			resetGame();
			return;
		}		
		for (Piece p : ps) {
			if (p.isRed && p.ypos == 0) {
				p.isKing = true;
			}
			if (!p.isRed && p.ypos == 7) {
				p.isKing = true;
			}
		}
		if(!isRedTurn) {
			//Calls minimax algorithm and returns most optimal move
			Move m = game.minimax(false, 6, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			
			//Updates visual position of bots pieces
			for(Piece p: ps) {
				if(m.fx==p.xpos && m.fy==p.ypos) {
					p.move(m.tx, m.ty);
					break;
				}
			}
			if(!m.isCapture) {
				game.doMove(false, m.fy, m.fx, m.ty, m.tx);
			} else {
				int[] jumpPos = {0,0};
				jumpPos = game.doJump(false, m.fy, m.fx, m.ty, m.tx);
				Iterator<Piece> iterator = ps.iterator();
				Piece capturedPiece = null;
				//Removes pieces captured by the bot
				while (iterator.hasNext()) {
					Piece p = iterator.next();
					if (p.ypos == jumpPos[0] && p.xpos == jumpPos[1]) {
						capturedPiece = p;
					}
				}
				if (capturedPiece != null) {
					ps.remove(capturedPiece);
				}
									
			}						
			completedMove();
		}
				
	}
	public static Piece getPiece(int x, int y)
	{
		int xpos = x/64;
		int ypos = y/64;
		for(Piece p : ps)
			if(p.xpos == xpos && p.ypos == ypos)
				return p;
		return null;
	}

	//Prints move as string
	public static Move splitParts(String s)
	{
		String[] parts = s.split(",");
		int[] nums = new int[4];
		String[] subparts1 = parts[0].split("-");
		nums[0] = Integer.parseInt(subparts1[0]);
		nums[1] = Integer.parseInt(subparts1[1]);
		String[] subparts2 = parts[1].split("-");
		nums[2] = Integer.parseInt(subparts2[0]);
		nums[3] = Integer.parseInt(subparts2[1]);
		return new Move(nums[0],nums[1],nums[2],nums[3],false);
	}
	public static ArrayList<Piece> addPieces(ArrayList<Piece> ps)
	{
		//Adds Pieces
		Piece black1 = new Piece(1, 0, false, false, ps);
		Piece black2 = new Piece(3, 0, false, false, ps);
		Piece black3 = new Piece(5, 0, false, false, ps);
		Piece black4 = new Piece(7, 0, false, false, ps);
		Piece black5 = new Piece(0, 1, false, false, ps);
		Piece black6 = new Piece(2, 1, false, false, ps);
		Piece black7 = new Piece(4, 1, false, false, ps);
		Piece black8 = new Piece(6, 1, false, false, ps);
		Piece black9 = new Piece(1, 2, false, false, ps);
		Piece black10 = new Piece(3, 2, false, false, ps);
		Piece black11 = new Piece(5, 2, false, false, ps);
		Piece black12 = new Piece(7, 2, false, false, ps);
		
		Piece red1 = new Piece(0, 5, true, false, ps);
		Piece red2 = new Piece(2, 5, true, false, ps);
		Piece red3 = new Piece(4, 5, true, false, ps);
		Piece red4 = new Piece(6, 5, true, false, ps);
		Piece red5 = new Piece(1, 6, true, false, ps);
		Piece red6 = new Piece(3, 6, true, false, ps);
		Piece red7 = new Piece(5, 6, true, false, ps);
		Piece red8 = new Piece(7, 6, true, false, ps);
		Piece red9 = new Piece(0, 7, true, false, ps);
		Piece red10 = new Piece(2, 7, true, false, ps);
		Piece red11 = new Piece(4, 7, true, false, ps);
		Piece red12 = new Piece(6, 7, true, false, ps);
			
		return ps;
	}
			
	public static void GameOver()
	{
		//Scanner scanner = new Scanner(System.in);
		
		//Tallies up the pieces remaining
		int redPiecesRemaining = 0;
		int blackPiecesRemaining = 0;
		for(Piece p : ps) {
			if(p.isRed) {
				redPiecesRemaining++;
			} else {
				blackPiecesRemaining++;
			}
		}
		System.out.println("\nThe game has concluded!");
		if(redPiecesRemaining>blackPiecesRemaining)
			System.out.println("Congradulations, You win!");
		if(redPiecesRemaining<blackPiecesRemaining)
			System.out.println("Alex's checkers bot won, better luck next time!");
		if(redPiecesRemaining==blackPiecesRemaining)
			System.out.println("Its a draw!");
		System.out.println("Would you like to play again?");

		resetGame();	
		//String input = scanner.nextLine();	
	}

	public static void resetGame() {
		moves.clear();
		ps.clear();
		isRedTurn = true;
		
		game = new Game();
		game.setBoard();
		addPieces(ps);
		moves = game.allMoves(isRedTurn);
	}
	
}
