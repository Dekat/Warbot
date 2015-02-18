package edu.warbot.FSM.plan;

import javax.swing.JOptionPane;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionSendMessage;
import edu.warbot.FSMEditor.settings.EnumMessage;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarPlanSendMessage<AgentAdapterType extends ControllableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	WarAgentType agentType;
	EnumMessage msg;
	
	public WarPlanSendMessage(AgentAdapterType brain, GenericPlanSettings planSettings) {
		super("Plan Attaquer", brain, planSettings);
		
		if(getPlanSettings().Agent_type != null)
			this.agentType = getPlanSettings().Agent_type;
		else
			JOptionPane.showMessageDialog(null, "You must chose <Agent_type> for plan <WarPlanSendMessage>", "Missing attribut", JOptionPane.ERROR_MESSAGE);
	
		if(getPlanSettings().Message != null)
			this.msg = getPlanSettings().Message;
		else
			JOptionPane.showMessageDialog(null, "You must chose <Message> for plan <WarPlanSendMessage>", "Missing attribut", JOptionPane.ERROR_MESSAGE);
	
		
	}

	public void buildActionList() {
		
		setPrintTrace(true);
			
		WarAction<AgentAdapterType> actionMsg = 
				new WarActionSendMessage<>(getBrain(), agentType, msg);
		
		addAction(actionMsg);
		
		setFirstAction(actionMsg);
	}
	
}
