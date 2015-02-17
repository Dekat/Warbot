package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionDefendre;
import edu.warbot.FSM.genericSettings.PlanSettings;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

/**
 * Réalise l'action WarActionDefendre 
 */
public class WarPlanDefendre extends WarPlan<WarRocketLauncherAdapter> {
	
	public WarPlanDefendre(WarRocketLauncherAdapter brain, PlanSettings planSettings) {
		super("Plan Defendre", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<WarRocketLauncherAdapter> actionDef = new WarActionDefendre(getBrain());
		addAction(actionDef);
		
		setFirstAction(actionDef);
	}
	
}
