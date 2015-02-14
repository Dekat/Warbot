package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionIdle;
import edu.warbot.FSM.action.WarActionWiggle;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Reste sans bougé
 * @author Olivier
 */
public class WarPlanIdle<AgentAdapterType extends ControllableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	private Integer nombreTik;

	public WarPlanIdle(AgentAdapterType brain, WarPlanSettings planSettings) {
		super("Plan Idle", brain, planSettings);
		this.nombreTik = planSettings.Time_out;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionAttaquer = new WarActionIdle<AgentAdapterType>(getBrain(), nombreTik);
		
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
}
