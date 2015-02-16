package edu.warbot.FSM.condition;

import edu.warbot.FSM.WarGenericSettings.ConditionSettings;
import edu.warbot.FSMEditor.settings.EnumMessage;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.communications.WarMessage;

public class WarConditionMessageCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType>{
	
	WarAgentType agentType;
	EnumMessage message;

	public WarConditionMessageCheck(String name, AgentAdapterType brain, ConditionSettings conditionSettings){
		super(name, brain, conditionSettings);
		
		this.message = conditionSettings.Message;
		this.agentType = conditionSettings.Agent_type;
	}

	@Override
	public boolean isValide() {
		for (WarMessage m : getBrain().getMessages()) {
			if(m.getMessage().equals(this.message.name()) 
					&& m.getSenderType().equals(agentType)){
				return true;
			}
		}
		return false;
	}
	
}
