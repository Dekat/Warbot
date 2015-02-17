package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.FSM.genericSettings.PlanSettings;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

public class WarPlanAttaquer extends WarPlan<WarRocketLauncherAdapter> {
	
	public WarPlanAttaquer(WarRocketLauncherAdapter brain, PlanSettings planSettings) {
		super("Plan Attaquer", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
			
		WarAction<WarRocketLauncherAdapter> actionAttaquer = 
				new WarActionAttaquer(getBrain(), getPlanSettings().Agent_type);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
