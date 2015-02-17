package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

public class WarPlanAttaquer extends WarPlan<WarRocketLauncherAdapter> {
	
	public WarPlanAttaquer(WarRocketLauncherAdapter brain, GenericPlanSettings planSettings) {
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
