package edu.warbot.brains;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.brains.capacities.Movable;

public abstract class MovableWarAgentAdapter extends ControllableWarAgentAdapter implements Movable {
		
	public MovableWarAgentAdapter(MovableWarAgent agent) {
		super(agent);
	}
	
	@Override
	protected MovableWarAgent getAgent() {
		return (MovableWarAgent) super.getAgent();
	}

	@Override
	public boolean isBlocked() {
		return getAgent().isBlocked();
	}
	
	@Override
	public double getSpeed() {
		return getAgent().getSpeed();
	}


	
}
