package edu.warbot.FSM.condition;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.communications.WarMessage;

public class WarConditionMessageChecker<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType>{
	
	WarAgentType agentType;
	String message;

	public WarConditionMessageChecker(AgentAdapterType brain,
			WarAgentType agentType, String message) {
		super(brain);
		
		this.message = message;
		this.agentType = agentType;
	}

	@Override
	public boolean isValide() {
		for (WarMessage m : getBrain().getMessages()) {
			if(m.getMessage().equals(this.message) && m.getSenderType().equals(agentType)){
				return true;
			}
		}
		return false;
	}
	
}
