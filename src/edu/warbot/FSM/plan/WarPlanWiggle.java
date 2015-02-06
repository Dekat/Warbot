package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.FSM.action.WarActionWiggle;
import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.WarAgentAdapter;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

/**
 * Avance de manière perdu
 * @author Olivier
 */
public class WarPlanWiggle extends WarPlan<MovableWarAgentAdapter> {
	
	
	public WarPlanWiggle(WarRocketLauncherAdapter brain, WarPlanSettings planSettings) {
		super("Plan Wiggle", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<MovableWarAgentAdapter> actionAttaquer = new WarActionWiggle<MovableWarAgentAdapter>(getBrain(), 100000);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
