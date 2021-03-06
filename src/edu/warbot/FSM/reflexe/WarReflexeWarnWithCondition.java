package edu.warbot.FSM.reflexe;

import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;

public class WarReflexeWarnWithCondition<BrainType extends WarBrain> extends WarReflexe<BrainType>{

	WarCondition<BrainType> condition;
	String message;
	WarAgentType agentType;
	
	public WarReflexeWarnWithCondition(BrainType b, WarCondition<BrainType> condition, WarAgentType agentType, String message) {
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
