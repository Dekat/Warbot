package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

public class WarPlanAttaquer extends WarPlan<WarRocketLauncherAdapter> {
	
	private WarAgentType agentType;

	public WarPlanAttaquer(WarRocketLauncherAdapter brain, GenericPlanSettings planSettings) {
		super("Plan Attaquer", brain, planSettings);
		
		if(getPlanSettings().Agent_type != null)
			this.agentType = getPlanSettings().Agent_type;
		else
			this.agentType = WarAgentType.WarRocketLauncher;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
			
		WarAction<WarRocketLauncherAdapter> actionAttaquer = 
				new WarActionAttaquer(getBrain(), agentType);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
