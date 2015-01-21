package edu.warbot.brains.capacities;

import edu.warbot.agents.enums.WarAgentType;

public interface CreatorBrain {

	public void setNextAgentToCreate(WarAgentType agent);
	public WarAgentType getNextAgentToCreate();
	public boolean isAbleToCreate(WarAgentType agent);	
	
}
