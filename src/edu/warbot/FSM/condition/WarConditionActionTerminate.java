package edu.warbot.FSM.condition;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionActionTerminate<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType>{
	
	WarAction<AgentAdapterType> action;
	
	public WarConditionActionTerminate(String name, AgentAdapterType brain, 
			WarConditionSettings conditionSettings){
		super(name, brain, conditionSettings);
		//TODO très attention ici !!!
		this.action = conditionSettings.Action;
	}

	@Override
	public boolean isValide() {
		if(this.action.isTerminate())
			return true;
		else
			return false;
			
	}

}
