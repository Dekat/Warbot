package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionWiggle;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Avance de manière perdu
 */
public class WarPlanWiggle<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	
	private Integer nombreTik;

	public WarPlanWiggle(AgentAdapterType brain, GenericPlanSettings planSettings) {
		super("Plan Wiggle", brain, planSettings);
		
		if(getPlanSettings().Tik_number != null)
			this.nombreTik = getPlanSettings().Tik_number;
		else
			this.nombreTik = 0;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionAttaquer = new WarActionWiggle<AgentAdapterType>(getBrain(),
				nombreTik);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
