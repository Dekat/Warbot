package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.PlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionSendMessage;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

public class WarPlanSendMessage extends WarPlan<WarRocketLauncherAdapter> {
	
	WarAgentType agentType;
	
	public WarPlanSendMessage(WarRocketLauncherAdapter brain, PlanSettings planSettings) {
		super("Plan Attaquer", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
			
		WarAction<WarRocketLauncherAdapter> actionMsg = 
				new WarActionSendMessage(getBrain(), getPlanSettings().Agent_type, getPlanSettings().Message);
		
		addAction(actionMsg);
		
		setFirstAction(actionMsg);
	}
	
}
