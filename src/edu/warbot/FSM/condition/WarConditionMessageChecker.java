package edu.warbot.FSM.condition;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.brains.WarRocketLauncherBrain;
import edu.warbot.communications.WarMessage;

public class WarConditionMessageChecker extends WarCondition{
	
	WarAgentType agentType;
	String message;

	public WarConditionMessageChecker(WarRocketLauncherBrain brain,
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
