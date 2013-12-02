/******************************************************************************
 *
 *    Representation of a board and the sequence of moves that generated it
 *
 *
 * @author Daniel R. Carvalho e Otavio R. Filho
 * @date 11/28/2013
 *****************************************************************************/
package mata64.agents;

import java.util.*;

public class TileBoard{

	//String representation of a puzzle board
	private Integer [][] myBoard = new Integer [3][3];
	//String representation of the list of moves that generated this board
	private List<Move> moves;
	
	private int g;
	private int f;

	public TileBoard(Integer[][] board, int g, List<Move> moveList){
      myBoard = board;
      if (moveList == null){
    	  moves = new ArrayList<Move>();
      }
      else{
    	  moves = moveList;
      }
      this.g = g;
      this.f = g + this.calcManhattanDistance();
	}
	
	public int getG(){
		return g;
	}
	
	public int getF(){
		return f;
	}
	
	public void setG(int g){
		this.g = g;
	}

	public void setF(int f){
		this.f = f;
	}
	
	/* Returns the number of moves from the initial board */
	public int getNumMoves(){
		return moves.size();
	}

	/* Evaluates the given board using the Manhattan distance heuristic */
	public int calcManhattanDistance(){
		Integer distance = 0;
		for (int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++) {
    		  Integer goalI = myBoard[i][j]/3;
    		  Integer goalJ = myBoard[i][j]%3;
    		  distance += Math.abs(i-goalI) + Math.abs(j-goalJ);
			}
		}

		return distance;
	}
	
	/* Returns the position of a number on the board */
	public Integer[] seekNumber(int number){
		Integer [] position = new Integer[2];
		for (int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++) {
				if (myBoard[i][j] == number){
					position[0] = i;
					position[1] = j;
				}
			}
		}
		return position;
	}

	/* Move the empty tile to the desired position */
	public TileBoard moveTile(int direction){
		int i, j;

		// Get the position of the empty tile
		Integer[] emptyTilePos = seekNumber(0);

		// Set new empty tile coordinates based on movement direction
		switch (direction){
		case Move.LEFT:
			i = emptyTilePos[0];
			j = emptyTilePos[1]-1;
			break;
		case Move.UP:
			i = emptyTilePos[0]-1;
			j = emptyTilePos[1];
			break;
		case Move.RIGHT:
			i = emptyTilePos[0];
			j = emptyTilePos[1]+1;
			break;
		default:
			i = emptyTilePos[0]+1;
			j = emptyTilePos[1];
			break;
		}
		
		// If invalid position, return null
		if ((i < 0)||(i > 2)||(j < 0)||(j > 2)){
			return null;
		}

		// Create new board with moved tile
		Integer[][] newBoard = new Integer[3][3];
		copyBoard(newBoard, myBoard);
		newBoard[emptyTilePos[0]][emptyTilePos[1]] = newBoard[i][j];
		newBoard[i][j] = 0;
		
		List<Move> newMovesList = new ArrayList<Move>();
		copyMoves(newMovesList, moves);
		newMovesList.add(new Move(direction));
		
		return new TileBoard(newBoard, g+1, newMovesList);
	}
	
	private void copyMoves(List<Move> dstMoveList, List<Move> srcMoveList){
		for (int i = 0; i < srcMoveList.size(); i++){
			dstMoveList.add(srcMoveList.get(i));
		}
	}
	
	private void copyBoard(Integer[][] dstBoard, Integer[][] srcBoard){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				dstBoard[i][j] = srcBoard[i][j];
			}
		}
	}
	
	public List<Move> getMoves(){
		return moves;
	}
	
	public Integer[][] getBoardMatrix(){
		return myBoard;
	}
	
	/* Returns the last move of the move list */
	public Move getLastMove(){
		if (moves.size() > 0){
			return moves.get(moves.size()-1);
		}
		else{
			return null;
		}
	}
	
	/*
	 * Returns a list of boards that are one move away.  This list *DOES NOT* contain the
	 * previous board, as this would undo a movement we've just made.
	 */
	public List<TileBoard> getNextBoards(){
		List<TileBoard> boardList = new ArrayList<TileBoard>();
		Move lastMove = this.getLastMove();
		int oppositeMove = -1;
		if (lastMove != null){
			oppositeMove = lastMove.getOppositeDirection();
		}

		// Find and add all possible movements
		TileBoard newBoard;
		if (oppositeMove != Move.UP){
			newBoard = this.moveTile(Move.UP);
			if (newBoard != null){
				boardList.add(newBoard);
			}
		}
		if (oppositeMove != Move.DOWN){
			newBoard = this.moveTile(Move.DOWN);
			if (newBoard != null){
				boardList.add(newBoard);
			}
		}
		if (oppositeMove != Move.LEFT){
			newBoard = this.moveTile(Move.LEFT);
			if (newBoard != null){
				boardList.add(newBoard);
			}
		}
		if (oppositeMove != Move.RIGHT){
			newBoard = this.moveTile(Move.RIGHT);
			if (newBoard != null){
				boardList.add(newBoard);
			}
		}
		
		return boardList;
	}
	
	public void printBoard(){
		int i, j;
		for (i = 0; i < 3; i++){
			for (j = 0; j < 2; j++){
				System.out.print(myBoard[i][j] + "|");
			}
			System.out.println(myBoard[i][j]);
		}
		System.out.println("Manhattan distance: "+this.calcManhattanDistance());
		System.out.println();
	}
}
