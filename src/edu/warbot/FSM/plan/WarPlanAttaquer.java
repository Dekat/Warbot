package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

public class WarPlanAttaquer extends WarPlan<WarRocketLauncherAdapter> {
	
//	WarAgentType agentType;
	
//	public WarPlanAttaquer(WarRocketLauncherAdapter brain, WarAgentType agentType) {
	public WarPlanAttaquer(WarRocketLauncherAdapter brain, WarPlanSettings planSettings) {
		super("Plan Attaquer", brain, planSettings);
//		this.agentType = agentType;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAgentType agentType = null;
		
		if(getPlanSettings().Agent_type_destination != null && getPlanSettings().Agent_type_destination.length > 0)
			agentType = getPlanSettings().Agent_type_destination[0];
		else
			System.err.println("getPlanSettings().Agent_type_destination is null or does not containe value in " + this.getClass().getSimpleName());
			
		WarAction<WarRocketLauncherAdapter> actionAttaquer = new WarActionAttaquer(getBrain(), agentType);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
