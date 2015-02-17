package edu.warbot.FSM.plan;

/**
 * Me heal
 */
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionHealMe;
import edu.warbot.FSM.genericSettings.PlanSettings;
import edu.warbot.brains.MovableWarAgentAdapter;

public class WarPlanHealMe<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	public WarPlanHealMe(AgentAdapterType brain, PlanSettings planSettings) {
		super("Plan heal me", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionHeal = new WarActionHealMe<>(getBrain());
		addAction(actionHeal);
		
		setFirstAction(actionHeal);
	}
	
}
