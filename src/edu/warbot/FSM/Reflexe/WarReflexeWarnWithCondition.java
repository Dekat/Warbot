package edu.warbot.FSM.Reflexe;

import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;

public class WarReflexeWarnWithCondition extends WarReflexe{

	WarCondition condition;
	String message;
	WarAgentType agentType;
	
	public WarReflexeWarnWithCondition(WarBrain b, WarCondition condition, WarAgentType agentType, String message) {
		super(b, "Reflexe warn wit condition");
		this.condition = condition;
		this.message = message;
		this.agentType = agentType;
	}

	@Override
	public String executeReflexe() {
		if(this.condition.isValide()){
			getBrain().broadcastMessageToAgentType(this.agentType, this.message, "");
		}
		return null;
	}

}
