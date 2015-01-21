package edu.warbot.brains;

import edu.warbot.agents.CreatorWarAgent;
import edu.warbot.agents.capacities.Creator;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.capacities.CreatorBrain;

public abstract class CreatorWarAgentBrain extends WarBrain implements CreatorBrain {

	public CreatorWarAgentBrain(CreatorWarAgent agent) {
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
