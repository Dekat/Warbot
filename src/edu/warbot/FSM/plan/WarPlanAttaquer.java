package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.PlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

public class WarPlanAttaquer extends WarPlan<WarRocketLauncherAdapter> {
	
	WarAgentType agentType;
	
	public WarPlanAttaquer(WarRocketLauncherAdapter brain, PlanSettings planSettings) {
		super("Plan Attaquer", brain, planSettings);
		
		this.agentType = getPlanSettings().Agent_type;
		
	}

	public void buildActionList() {
		
		setPrintTrace(true);
			
		WarAction<WarRocketLauncherAdapter> actionAttaquer = new WarActionAttaquer(getBrain(), agentType);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
