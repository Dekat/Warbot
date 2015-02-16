package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.PlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionCreateUnit;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarBaseAdapter;

/**
 * Desciption du plan et de ces actions
 * ATTENTION ce plan est bas� sur la size bag d'un explorer et la vie max d'un explorer
 */
public class WarPlanCreateUnit extends WarPlan<WarBaseAdapter>{
	
	Integer nombreAgent;
	WarAgentType agentType;
	
	int minLife;
	boolean pourcentage;
	
	public WarPlanCreateUnit(WarBaseAdapter brain, PlanSettings planSettings ) {
		super("Plan healer", brain, planSettings);
		
		this.agentType = getPlanSettings().Agent_type;
		this.nombreAgent = getPlanSettings().Number_agent;
		
		this.pourcentage = getPlanSettings().Pourcentage;
		
		this.minLife = getPlanSettings().Min_life;
		
		if(pourcentage)
			minLife = (int) (WarBase.MAX_HEALTH*minLife/100);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<WarBaseAdapter> actionC = new WarActionCreateUnit(getBrain(), agentType, nombreAgent, minLife);
		this.addAction(actionC);
		
		setFirstAction(actionC);
	}
	
}
