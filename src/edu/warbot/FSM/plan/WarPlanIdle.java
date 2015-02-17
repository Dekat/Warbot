package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionIdle;
import edu.warbot.FSM.genericSettings.PlanSettings;
import edu.warbot.brains.ControllableWarAgentAdapter;

/**
 * Reste sans bougé
 */
public class WarPlanIdle<AgentAdapterType extends ControllableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	private Integer nombreTik;

	public WarPlanIdle(AgentAdapterType brain, PlanSettings planSettings) {
		super("Plan Idle", brain, planSettings);
		
		if(getPlanSettings().Tik_number != null)
			this.nombreTik = getPlanSettings().Tik_number;
		else
			this.nombreTik = 0;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionAttaquer = new WarActionIdle<>(getBrain(), nombreTik);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
