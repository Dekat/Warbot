package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

public class WarPlanAttaquer extends WarPlan<WarRocketLauncherAdapter> {
	
	WarAgentType agentType;
	
	public WarPlanAttaquer(WarRocketLauncherAdapter brain, WarAgentType agentType) {
		super(brain, "Plan Attaquer");
		this.agentType = agentType;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<WarRocketLauncherAdapter> actionAttaquer = new WarActionAttaquer(getBrain(), this.agentType);
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
