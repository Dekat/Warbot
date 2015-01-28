package edu.warbot.FSM.condition;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionActionTerminate<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType>{
	
	WarAction<AgentAdapterType> action;
	
	public WarConditionActionTerminate(AgentAdapterType brain, WarAction<AgentAdapterType> action){
		super(brain);
		this.action = action;
	}

	@Override
	public boolean isValide() {
		if(this.action.isTerminate())
			return true;
		else
			return false;
			
	}

}
