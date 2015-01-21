package edu.warbot.brains;

import edu.warbot.agents.MovableWarAgent;

public abstract class MovableWarAgentBrain extends WarBrain {
		
	public MovableWarAgentBrain(MovableWarAgent agent) {
		super(agent);
	}

	public boolean isBlocked() {
		return ((MovableWarAgent) _agent).isBlocked();
	}
	
}
