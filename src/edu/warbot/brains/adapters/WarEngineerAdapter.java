package edu.warbot.brains.adapters;

import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.capacities.Creator;

public class WarEngineerAdapter extends MovableWarAgentAdapter implements Creator {

	public WarEngineerAdapter(WarEngineer agent) {
		super(agent);
	}

	@Override
	protected WarEngineer getAgent() {
		return (WarEngineer) super.getAgent();
	}
	
	@Override
	public void setNextAgentToCreate(WarAgentType nextAgentToCreate) {
		getAgent().setNextAgentToCreate(nextAgentToCreate);
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
