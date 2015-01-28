package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionDefendre;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

/**
 * @author Olivier
 *
 */
public class WarPlanDefendre extends WarPlan<WarRocketLauncherAdapter> {
	
	public WarPlanDefendre(WarRocketLauncherAdapter brain) {
		super(brain, "Plan Defendre");
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<WarRocketLauncherAdapter> actionDef = new WarActionDefendre(getBrain());
		addAction(actionDef);
		
		setFirstAction(actionDef);
	}
	
}
