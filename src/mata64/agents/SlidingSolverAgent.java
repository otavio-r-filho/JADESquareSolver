package mata64.agents;

import jade.core.Agent;

public class SlidingSolverAgent extends Agent{
	public void setup(){
		// Print a start message
		System.out.println("Agent " + getAID().getName() + " is starting");
		
		addBehaviour(new SlidingSolver(this, new Integer[][]{{0, 4, 2}, {1, 3, 7}, {6, 8, 5}}));
		
		//doDelete();
	}
	
	protected void takeDown(){
		// Print termination message
		System.out.println("Agent " + getAID().getName() + " is terminating.");
	}
}
