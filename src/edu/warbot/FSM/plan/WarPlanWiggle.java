package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionWiggle;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

/**
 * Avance de manière perdu
 * @author Olivier
 */
public class WarPlanWiggle<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	
	public WarPlanWiggle(AgentAdapterType brain, WarPlanSettings planSettings) {
		super("Plan Wiggle", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionAttaquer = new WarActionWiggle<AgentAdapterType>(getBrain(), 100000);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
