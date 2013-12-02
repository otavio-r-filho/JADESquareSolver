package mata64.agents;

public class Move{
	public static final int LEFT = 0;
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;

	private int direction;
	private int oppositeDirection;

	public Move(int dir){
		direction = dir;
		oppositeDirection = (direction + 2)%4;
	}
	
	public int getDirection(){
		return direction;
	}
	
	public int getOppositeDirection(){
		return oppositeDirection;
	}
}