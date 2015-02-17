package edu.warbot.FSM.condition;

import javax.swing.JOptionPane;

import edu.warbot.FSM.genericSettings.ConditionSettings;
import edu.warbot.FSMEditor.settings.EnumMessage;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.communications.WarMessage;

public class WarConditionMessageCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType>{
	
	WarAgentType agentType;
	EnumMessage message;

	public WarConditionMessageCheck(String name, AgentAdapterType brain, ConditionSettings conditionSettings){
		super(name, brain, conditionSettings);
		
		if(conditionSettings.Message != null)
			this.message = conditionSettings.Message;
		else
			JOptionPane.showMessageDialog(null, "You must chose <Message> for condition <WarConditionMessageCheck>", "Missing attribut", JOptionPane.ERROR_MESSAGE);
		
		if(conditionSettings.Agent_type != null)
			this.agentType = conditionSettings.Agent_type;
		else
			JOptionPane.showMessageDialog(null, "You must chose <Agent_type> for condition <WarConditionMessageCheck>", "Missing attribut", JOptionPane.ERROR_MESSAGE);
		
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
