package edu.warbot.agents.capacities;

import edu.warbot.agents.enums.WarAgentType;

public interface Creator {

	public static String ACTION_CREATE = "create";
	
	public String create();

	public boolean isAbleToCreate(WarAgentType agent);
	public void setNextAgentToCreate(WarAgentType nextAgentToCreate);
	public WarAgentType getNextAgentToCreate();
}
