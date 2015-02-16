package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.PlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionHealMe;
import edu.warbot.brains.MovableWarAgentAdapter;

public class WarPlanHealMe<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	public WarPlanHealMe(AgentAdapterType brain, PlanSettings planSettings) {
		super("Plan healer", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionHeal = new WarActionHealMe<>(getBrain());
		addAction(actionHeal);
		
		setFirstAction(actionHeal);
	}
	
}
