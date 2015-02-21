package edu.warbot.FSM.plan;

/**
 * Me heal
 */
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionHealMe;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarPlanHealMe<AgentAdapterType extends ControllableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	public WarPlanHealMe(AgentAdapterType brain, GenericPlanSettings planSettings) {
		super("Plan heal myself", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionHeal = new WarActionHealMe<>(getBrain());
		addAction(actionHeal);
		
		setFirstAction(actionHeal);
	}
	
}
