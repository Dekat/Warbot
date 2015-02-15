package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionWiggle;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

/**
 * Avance de manière perdu
 * @author Olivier
 */
public class WarPlanWiggle<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	
	private Integer nombrePas;

	public WarPlanWiggle(AgentAdapterType brain, WarPlanSettings planSettings) {
		super("Plan Wiggle", brain, planSettings);
		this.nombrePas = planSettings.Tik_number;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionAttaquer = new WarActionWiggle<AgentAdapterType>(getBrain(),
				nombrePas);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
