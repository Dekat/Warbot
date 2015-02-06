package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionChercherNouriture;
import edu.warbot.FSM.action.WarActionHeal;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionActionTerminate;
import edu.warbot.FSM.condition.WarConditionAttributCheck;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Desciption du plan et de ces actions
 * ATTENTION ce plan est basé sur la size bag d'un explorer et la la vie max d'un explorer
 * @author Olivier
 *
 */
public class WarPlanHealer<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	boolean healAlly = true;
	
	int pourcentageLife;
	
	int pourcentageLifeAlly;

	//  lifePourcentage, int lifePourcentageAlly
	public WarPlanHealer(AgentAdapterType brain, WarPlanSettings planSettings) {
		super("Plan healer", brain, planSettings);
		
		this.pourcentageLife = getPlanSettings().Value_pourcentage;
		this.pourcentageLifeAlly = getPlanSettings().Value_pourcentage_destination;
		
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionHeal = new WarActionHeal<AgentAdapterType>(getBrain(), this.pourcentageLife, 
				this.pourcentageLifeAlly, healAlly);
		addAction(actionHeal);
		
		WarAction<AgentAdapterType> actionFindFood = new WarActionChercherNouriture<AgentAdapterType>(getBrain(), WarExplorer.BAG_SIZE);
		addAction(actionFindFood);
		
		WarCondition<AgentAdapterType> condTerminateHeal = new WarConditionActionTerminate<AgentAdapterType>(getBrain(), actionHeal);
		condTerminateHeal.setDestination(actionFindFood);
		actionHeal.addCondition(condTerminateHeal);
		
		WarCondition<AgentAdapterType> condTerminateFindFood = new WarConditionActionTerminate<AgentAdapterType>(getBrain(), actionFindFood);
		condTerminateFindFood.setDestination(actionHeal);
		actionFindFood.addCondition(condTerminateFindFood);
		
		WarCondition<AgentAdapterType> condLowLife = new WarConditionAttributCheck<AgentAdapterType>(getBrain(), WarConditionAttributCheck.HEALTH, "<", WarExplorer.MAX_HEALTH, 50);
		condLowLife.setDestination(actionHeal);
		actionFindFood.addCondition(condLowLife);
		
		setFirstAction(actionHeal);
	}
	
}
