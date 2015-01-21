package edu.warbot.brains.brains;

import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.capacities.Creator;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.MovableWarAgentBrain;
import edu.warbot.brains.capacities.CreatorBrain;

public class WarEngineerBrain extends MovableWarAgentBrain implements CreatorBrain {

	public WarEngineerBrain(WarEngineer agent) {
		super(agent);
	}
	
	@Override
	public void setNextAgentToCreate(WarAgentType agent) {
		((Creator) _agent).setNextAgentToCreate(agent);
	}

	@Override
	public WarAgentType getNextAgentToCreate() {
		return ((Creator) _agent).getNextAgentToCreate();
	}

	@Override
	public boolean isAbleToCreate(WarAgentType agent) {
		return ((Creator) _agent).isAbleToCreate(agent);
	}
}
