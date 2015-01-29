package edu.warbot.brains;

import edu.warbot.agents.CreatorWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.capacities.Creator;

public abstract class CreatorWarAgentAdapter extends ControllableWarAgentAdapter implements Creator {

	public CreatorWarAgentAdapter(CreatorWarAgent agent) {
		super(agent);
	}
	
	@Override
	protected CreatorWarAgent getAgent() {
		return (CreatorWarAgent) super.getAgent();
	}

	@Override
	public void setNextAgentToCreate(WarAgentType agent) {
		getAgent().setNextAgentToCreate(agent);
	}

	@Override
	public WarAgentType getNextAgentToCreate() {
		return getAgent().getNextAgentToCreate();
	}

	@Override
	public boolean isAbleToCreate(WarAgentType agent) {
		return getAgent().isAbleToCreate(agent);
	}
	
}
