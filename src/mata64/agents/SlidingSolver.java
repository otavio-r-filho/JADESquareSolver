package mata64.agents;

import java.util.*;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.Agent;

public class SlidingSolver extends OneShotBehaviour{
	// Max depth of the algorithm
	private static int maxDepth = 50;
	private static List<TileBoard> closedList;
	private static List<TileBoard> openList;
	private TileBoard solutionBoard = null;

	public SlidingSolver(Agent a, Integer[][] initialBoardMatrix){
		super(a);
		
		// Initialize queues and puzzle
		closedList = new ArrayList<TileBoard>();
		openList = new ArrayList<TileBoard>();
		
		openList.add(new TileBoard(initialBoardMatrix, 0, null));

		// Print initial board
		System.out.println("The initial board is:");
		openList.get(0).printBoard();
	}
	
	/* Solves the puzzle by performing an A* search over the puzzle space */
	public void action(){
		boolean tooDeep = false;

        // While there are still moves to do, and the goal hasn't been achieved, search
		while (!openList.isEmpty()&&(solutionBoard == null)&&(!tooDeep)){
			// Get next board to be analyzed (from the openList) and add it to the closedList
			TileBoard currentBoard = getSmallestCostBoard();
			System.out.println("Cost so far: "+currentBoard.getG());
			System.out.println("Using smallest:");
			currentBoard.printBoard();
			closedList.add(currentBoard);
			
			// Check if reached goal state or reached max number of moves
			if (currentBoard.calcManhattanDistance() == 0){
				System.out.println("FOUND SOLUTION!");
				solutionBoard = currentBoard;
			}
			else{
			  if (currentBoard.getG() > maxDepth){
				  tooDeep = true;
			  }
			  else{
				  // If it hasn't, check all possible moves (neighbors) from that state
				  List<TileBoard> neighbors = currentBoard.getNextBoards();
					System.out.println("Possible moves: "+neighbors.size());
				  for (int i = 0; i < neighbors.size(); i++){
					  TileBoard neighbor = neighbors.get(i);
						neighbor.printBoard();
					
					  // Check if neighbor is in closedList
					  // If so, get next board, as it has already been analysed
					  if (searchBoard(neighbor, 1) >= 0){
						  continue;
					  }
					
					  // If not explored yet, check if neighbor is in openList
					  int boardPosition = searchBoard(neighbor, 0);
					  // If it's not in openList, add it to it
					  if (boardPosition == -1){
						  openList.add(neighbor);
					  }
					  else{
						  // Else, check if it's F is smaller than the previous one
						  if (neighbor.getF() < openList.get(boardPosition).getF()){
							  // If so, update F
							  openList.get(boardPosition).setG(neighbor.getG());
							  openList.get(boardPosition).setF(neighbor.getF());
						  }
					  }
				  }
			  }
			}
		}
		// Show solution, if it exists
		if (solutionBoard != null){
			showSolution();
		}
		else{
			System.out.println("Couldn't find final state");
		}
	}
	
	/* Search closed or open list based on the listNum argument for a given tileboard */
	public int searchBoard(TileBoard searched, int listNum){
		// Search on openList
		if (listNum == 0){
			for (int i = 0; i < openList.size(); i++){
				if (equalBoards(openList.get(i), searched) == 1){
					return i;
				}
			}
		}
		// Search on closedList
		else{
			for (int i = 0; i < closedList.size(); i++){
				if (equalBoards(closedList.get(i), searched) == 1){
					return i;
				}
			}
		}
		
		return -1;
	}
	
	/* Get smallest cost board from the open list */
	public TileBoard getSmallestCostBoard(){
		int smallestI = 0;
		for (int i = 0; i < openList.size(); i++){
			if (openList.get(i).calcManhattanDistance() < openList.get(smallestI).calcManhattanDistance()){
				smallestI = i;
			}
		}
		return openList.remove(smallestI);
	}

  /* Show all moves needed to solve the board */
	private void showSolution(){
		List<Move> moveList = solutionBoard.getMoves();

		System.out.println("Moves needed to solve board:");
		for (int i = 0; i < moveList.size(); i++){
			if (i%10 == 9){
				System.out.println();
			}
			String direction;
			switch(moveList.get(i).getDirection()){
			case (Move.UP):
				direction = "UP";
				break;
			case (Move.LEFT):
				direction = "LEFT";
				break;
			case (Move.DOWN):
				direction = "DOWN";
				break;
			default:
				direction = "RIGHT";
				break;
			}
			System.out.print(direction + " ");
		}
		System.out.println();
	}

  /* Compare two boards to check if they're equal */
	public int equalBoards(TileBoard a, TileBoard b){
		Integer[][] aMatrix = a.getBoardMatrix();
		Integer[][] bMatrix = b.getBoardMatrix();
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if (aMatrix[i][j] != bMatrix[i][j]){
					return 0;
				}
			}
		}
		return 1;
	}
}
