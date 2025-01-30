package design_patterns.checkers;

import java.util.ArrayList;
import java.lang.Math;

public class Game {
	private int[][] board = new int[8][8];
	static final int
    EMPTY = 0,
    RED = 1,
    RED_KING = 2,
    BLACK = 3,
    BLACK_KING = 4;
	
	//Array to store the game state
	final int[][] NEWBOARD = 
		  {{0, 3, 0, 3, 0, 3, 0, 3},
		   {3, 0, 3, 0, 3, 0, 3, 0},
		   {0, 3, 0, 3, 0, 3, 0, 3},
		   {0, 0, 0, 0, 0, 0, 0, 0},
		   {0, 0, 0, 0, 0, 0, 0, 0},
		   {1, 0, 1, 0, 1, 0, 1, 0},
		   {0, 1, 0, 1, 0, 1, 0, 1},
		   {1, 0, 1, 0, 1, 0, 1, 0}};
	//Array to store the location point boost for the red pieces
	final double[][] PIECEVALUESRED = 
		  {{0, .7, 0, .8, 0, .8, 0, .7},
		   {.6, 0, .7, 0, .7, 0, .6, 0},
		   {0, .5, 0, .6, 0, .6, 0, .5},
		   {.4, 0, .5, 0, .5, 0, .4, 0},
		   {0, .3, 0, .4, 0, .4, 0, .3},
		   {.2, 0, .3, 0, .3, 0, .2, 0},
		   {0, .1, 0, .2, 0, .2, 0, .1},
		   {0, 0, .2, 0, 0, 0, .2, 0}};
		   //Array to store the location point boost for the black pieces
	final double[][] PIECEVALUESBLACK = 
		  {{0, .2, 0, 0, 0, .2, 0, 0},
		   {.1, 0, .2, 0, .2, 0, .1, 0},
		   {0, .2, 0, .3, 0, .3, 0, .2},
		   {.3, 0, .4, 0, .4, 0, .3, 0},
		   {0, .4, 0, .5, 0, .5, 0, .4},
		   {.5, 0, .6, 0, .6, 0, .5, 0},
		   {0, .6, 0, .7, 0, .7, 0, .6},
		   {.7, 0, .8, 0, .8, 0, .7, 0}};
	void setBoard()
	{	
		board = NEWBOARD;
	}
	

	//Prints board as text (useful for debugging)
    void printBoard()
	{
		for(int i=0;i<17;i++)
		{
			if(i%2==0)
				System.out.println("+---+---+---+---+---+---+---+---+");
			else
				System.out.println("| " + board[i/2][0] + " | " + board[i/2][1] + " | " + board[i/2][2] + " | " + board[i/2][3] + " | " +
										  board[i/2][4] + " | " + board[i/2][5] + " | " + board[i/2][6] + " | " + board[i/2][7] + " | " + i/2);
		}	
		System.out.println("  0   1   2   3   4   5   6   7");
	}	
	
	/**
	 * The method itterates over every square that can be occupied. If there is a piece on the square that matches which players turn it is,
	 * then the squares surrounding it are checked to see if a move or capture is legal to that position. If it is, the move gets added to the 'moves'
	 * list which eventy gets returned to the caller.
	 */
    ArrayList<Move> allMoves(boolean isRedTurn)
    {
    	ArrayList<Move> moves = new ArrayList<>();
		int r = 0;
		int c = 0;;
		for(int i=1;i<=32;i++)
		{
			r = (i-1)/4;
			if(r%2==0)
				c = ((i+3)%4)*2+1;
			else
				c = ((i+3)%4)*2;
			
			if((board[r][c] == RED || board[r][c] == RED_KING) && isRedTurn)
    		{
    			 if(canMove(isRedTurn,r,c,r-1,c-1))
    			 {
    				 Move m = new Move(r, c, r-1, c-1, false);
    				 moves.add(m);
    			 }
    			 if(canMove(isRedTurn,r,c,r-1,c+1))
    			 {
    				 Move m = new Move(r, c, r-1, c+1, false);
    				 moves.add(m);
    			 }
    			 if(canJump(isRedTurn,r,c,r-2,c-2))
    			 {
    				 Move m = new Move(r, c, r-2, c-2, true);
    				 moves.add(m);
    			 }
    			 if(canJump(isRedTurn,r,c,r-2,c+2))
    			 {
    				 Move m = new Move(r, c, r-2, c+2, true);
    				 moves.add(m);
    			 }
    			 if(board[r][c] == RED_KING)
    			 {
    				 if(canMove(isRedTurn,r,c,r+1,c-1))
        			 {
        				 Move m = new Move(r, c, r+1, c-1, false);
        				 moves.add(m);
        			 }
        			 if(canMove(isRedTurn,r,c,r+1,c+1))
        			 {
        				 Move m = new Move(r, c, r+1, c+1, false);
        				 moves.add(m);
        			 }
        			 if(canJump(isRedTurn,r,c,r+2,c-2))
        			 {
        				 Move m = new Move(r, c, r+2, c-2, true);
        				 moves.add(m);
        			 }
        			 if(canJump(isRedTurn,r,c,r+2,c+2))
        			 {
        				 Move m = new Move(r, c, r+2, c+2, true);
        				 moves.add(m);
        			 }
    			 }
    		}
			else if((board[r][c] == BLACK || board[r][c] == BLACK_KING) && !isRedTurn)
    		{
    			 if(canMove(isRedTurn,r,c,r+1,c-1))
    			 {
    				 Move m = new Move(r, c, r+1, c-1, false);
    				 moves.add(m);
    			 }
    			 if(canMove(isRedTurn,r,c,r+1,c+1))
    			 {
    				 Move m = new Move(r, c, r+1, c+1, false);
    				 moves.add(m);
    			 }
    			 if(canJump(isRedTurn,r,c,r+2,c-2))
    			 {
    				 Move m = new Move(r, c, r+2, c-2, true);
    				 moves.add(m);
    			 }
    			 if(canJump(isRedTurn,r,c,r+2,c+2))
    			 {
    				 Move m = new Move(r, c, r+2, c+2, true);
    				 moves.add(m);
    			 }
    			 if(board[r][c] == BLACK_KING)
    			 {
    				 if(canMove(isRedTurn,r,c,r-1,c-1))
        			 {
        				 Move m = new Move(r, c, r-1, c-1, false);
        				 moves.add(m);
        			 }
        			 if(canMove(isRedTurn,r,c,r-1,c+1))
        			 {
        				 Move m = new Move(r, c, r-1, c+1, false);
        				 moves.add(m);
        			 }
        			 if(canJump(isRedTurn,r,c,r-2,c-2))
        			 {
        				 Move m = new Move(r, c, r-2, c-2, true);
        				 moves.add(m);
        			 }
        			 if(canJump(isRedTurn,r,c,r-2,c+2))
        			 {
        				 Move m = new Move(r, c, r-2, c+2, true);
        				 moves.add(m);
        			 }
    			 }
    		}
		}
    	
		return moves;
    }
	//Moves selected piece to its new position, and removes itself from its old location
	void doMove(boolean isRedTurn, int r1, int c1, int r2, int c2)
	{
		if(canMove(isRedTurn, r1, c1, r2, c2))
		{

			board[r2][c2] = board[r1][c1];
			board[r1][c1] = EMPTY;
		}

		checkPromote(isRedTurn, r2, c2);
	}
	//Moves selected piece to its new position, then clears the old location and the location if the piece that got captured
	int[] doJump(boolean isRedTurn, int r1, int c1, int r3, int c3)
	{
		int r2 = 0,c2 = 0;
		if(canJump(isRedTurn, r1, c1, r3, c3))
		{
			r2 = (r1+r3)/2;
			c2 = (c1+c3)/2;
			board[r3][c3] = board[r1][c1];
			board[r2][c2] = EMPTY;
			board[r1][c1] = EMPTY;
		}
		int[] pos = {r2,c2};

		checkPromote(isRedTurn, r3, c3);
		return pos;
	}

	//Checks if a piece reached the last row of the board, and promotes it to a king if it does
	void checkPromote(boolean isRedTurn, int row, int col) {
		if(isRedTurn && row==0)
			board[row][col] = 2;
		if(!isRedTurn && row==7)
			board[row][col] = 4;
	}
	//Checks if a move is legal
	boolean canMove(boolean isRedTurn, int r1, int c1, int r2, int c2)
	{
		if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
            return false; 
		if (board[r2][c2] != EMPTY)
            return false; 
		return true;
		
	}
	//Checks if a jump is legal
	boolean canJump(boolean isRedTurn, int r1, int c1, int r3, int c3)
	{
		int r2 = (r1+r3)/2;
		int c2 = (c1+c3)/2;
		
		if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
            return false; 
		if (board[r3][c3] != EMPTY)
            return false; 
		
		if(isRedTurn)
		{
			if ((board[r1][c1] == RED || board[r1][c1] == RED_KING) && Math.abs(r3 - r1) != 2)
                return false;  
            if (!(board[r2][c2] == BLACK || board[r2][c2] == BLACK_KING))
                return false;  
		}
		else 
		{
			if ((board[r1][c1] == BLACK || board[r1][c1] == BLACK_KING) && Math.abs(r3 - r1) != 2)
                return false; 
			if (!(board[r2][c2] == RED || board[r2][c2] == RED_KING))
                return false;  
		}
		
		return true;
	}
	/*void canChainJump(boolean isRedTurn, int r, int c, ArrayList<Move> mv)
	{
		if(isRedTurn)
		{
			if(canJump(isRedTurn, r, c, r+2, c-2))
			{
				Move m = new Move(r, c, r+2, c-2, true);
				mv.add(m);
				canChainJump(isRedTurn, r+2, c-2, mv);
			}
			if(canJump(isRedTurn, r, c, r+2, c+2))
			{
				Move m = new Move(r, c, r+2, c+2, true);
				mv.add(m);
				canChainJump(isRedTurn, r+2, c+2, mv);
			}
		}
		
	}*/

	/*
	 * Method is used to evaluate a position and return a double based on the result.
	 * A negative evaluation means that black is ahead, while a positive one means that white has the advantage.
	 * Many factors go into determining who has the advantage, such as number of pieces, types of pieces, and location of pieces.
	 * Regular peices are given one poing and kings are given two.
	 * Pieces are also given an advantage based on how close to the center they are and how close they are to promoting.
	 */
	double evaluatePosition() {
		double eval = 0.0;
		int redPieces = 0;
		int blackPieces = 0;
	
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == 1) { // Red normal piece
					eval += 1 + PIECEVALUESRED[i][j];
					redPieces++;
				}
				else if (board[i][j] == 2) { // Red king
					eval += 2 + (.7-PIECEVALUESRED[i][j]);
					redPieces++;
				}
				else if (board[i][j] == 3) { // Black normal piece
					eval -= 1 + PIECEVALUESBLACK[i][j];
					blackPieces++;
				}
				else if (board[i][j] == 4) { // Black king 
					eval -= 2 + (.7-PIECEVALUESBLACK[i][j]);
					blackPieces++;
				}
			}
		}
	
		// Check if the game is won/lost
		if (redPieces == 0) return Double.NEGATIVE_INFINITY; // Black wins
		if (blackPieces == 0) return Double.POSITIVE_INFINITY; // Red wins
	
		return eval;
	}

	/**
	 * Minimax with Alpha Beta pruning
	 * Minimax is a common algorithm used in two-player turn-based games.
	 * A basic minimax algorithm simulates all possible moves and evaluates each one, assuming both players are making optimal moves.
	 * A negative evaluation score represents an advantage for black, while a positive score represents an advantage for white.
	 * This program has its depth set to six, meaning that the computer evaluates three moves into the future for both players.
	 * When the computer (black) makes a move that will decrease the evaluation score the most, it will know which move from red will increase the evaluation the most.
	 * After it repeats this process until the given depth, it will know which moves give itself the best advantage if red plays perfectly.
	 * 
	 * The program also uses Alpha Beta pruning to break off (or prune) certain branches of moves that it knows are not optimal.
	 * If the program determines that a certain branch of the move tree is not optimal, it will stop evaluating the branch and move on.
	 * This greatly improves the efficiency of the program; I was able to increase the depth to six as opposed to four, while still having the bot's responses be instant.
	 * 
	 * Useful resource I found explaining the Minimax algorithm and Alpha Beta pruning: https://www.youtube.com/watch?v=l-hh51ncgDI&t=501s
	 */

	Move minimax(boolean isRedTurn, int depth, double alpha, double beta) {
		ArrayList<Move> possibleMoves = allMoves(isRedTurn);
		Move bestMove = null;
		
		if (isRedTurn) {
			double bestScore = Double.NEGATIVE_INFINITY;
			for (Move move : possibleMoves) {
				int[][] copy = copyBoard();
				if (!move.isCapture)
					doMove(isRedTurn, move.fy, move.fx, move.ty, move.tx);
				else
					doJump(isRedTurn, move.fy, move.fx, move.ty, move.tx);
	
				double score = minimaxHelper(!isRedTurn, depth - 1, alpha, beta);
	
				restoreBoard(copy);
	
				if (score > bestScore) {
					bestScore = score;
					bestMove = move;
				}
	
				alpha = Math.max(alpha, bestScore);
				if (beta <= alpha) {
					break;
				}
			}
			return bestMove;
		} else {
			double bestScore = Double.POSITIVE_INFINITY;
			for (Move move : possibleMoves) {
				int[][] copy = copyBoard();
				if (!move.isCapture)
					doMove(isRedTurn, move.fy, move.fx, move.ty, move.tx);
				else
					doJump(isRedTurn, move.fy, move.fx, move.ty, move.tx);
	
				double score = minimaxHelper(!isRedTurn, depth - 1, alpha, beta);
	
				restoreBoard(copy);
	
				if (score < bestScore) {
					bestScore = score;
					bestMove = move;
				}
	
				beta = Math.min(beta, bestScore);
				if (beta <= alpha) {
					break;
				}
			}
			return bestMove;
		}
	}

    private double minimaxHelper(boolean isRedTurn, int depth, double alpha, double beta) {
		if (depth == 0) {
			return evaluatePosition();
		}
	
		ArrayList<Move> possibleMoves = allMoves(isRedTurn);
		double bestScore = isRedTurn ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
	
		for (Move move : possibleMoves) {
			int[][] copy = copyBoard();
			if (!move.isCapture)
				doMove(isRedTurn, move.fy, move.fx, move.ty, move.tx);
			else
				doJump(isRedTurn, move.fy, move.fx, move.ty, move.tx);
	
			double score = minimaxHelper(!isRedTurn, depth - 1, alpha, beta);
	
			restoreBoard(copy);
	
			if (isRedTurn) {
				bestScore = Math.max(bestScore, score);
				alpha = Math.max(alpha, bestScore);
			} else {
				bestScore = Math.min(bestScore, score);
				beta = Math.min(beta, bestScore);
			}
	
			if (beta <= alpha) {
				break;
			}
		}
	
		return bestScore;
	}

	// copyBoard and restoreBoard are used in the minimax algorithm to play out moves and undo them to detirmine what move gives the biggest advantage
    private int[][] copyBoard() {
    	int[][] copy = new int[8][8];
        for (int i = 0; i < 8; i++) 
            for (int j = 0; j < 8; j++) 
                copy[i][j] = board[i][j];   
        return copy;
    }
    private void restoreBoard(int[][] copy) {
        for (int i = 0; i < 8; i++) 
            for (int j = 0; j < 8; j++) 
                board[i][j] = copy[i][j];   
    }

    
	void promote(int r, int c, int p)
	{
		board[r][c] = p;
	}
}

