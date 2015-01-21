package edu.warbot.agents.capacities;

public interface Movable {

	public static String ACTION_MOVE = "move";
	
	public String move();
	public boolean isBlocked();
	public double getSpeed();
	
}
