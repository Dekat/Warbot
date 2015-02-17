package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionSendMessage;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarPlanSendMessage<AgentAdapterType extends ControllableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	WarAgentType agentType;
	
	public WarPlanSendMessage(AgentAdapterType brain, GenericPlanSettings planSettings) {
		super("Plan Attaquer", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
			
		WarAction<AgentAdapterType> actionMsg = 
				new WarActionSendMessage<>(getBrain(), getPlanSettings().Agent_type, getPlanSettings().Message);
		
		addAction(actionMsg);
		
		setFirstAction(actionMsg);
	}
	
}
