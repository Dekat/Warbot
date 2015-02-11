package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
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
		
		WarConditionSettings condSet1 = new WarConditionSettings();
		condSet1.Action = actionHeal;
		WarCondition<AgentAdapterType> condTerminateHeal = 
		new WarConditionActionTerminate<AgentAdapterType>("cond_heal", getBrain(), condSet1);
		condTerminateHeal.setDestination(actionFindFood);
		actionHeal.addCondition(condTerminateHeal);
		
		WarConditionSettings condSet2 = new WarConditionSettings();
		condSet2.Action = actionFindFood;
		WarCondition<AgentAdapterType> condTerminateFindFood = 
				new WarConditionActionTerminate<AgentAdapterType>("cond_food", getBrain(), condSet2);
		condTerminateFindFood.setDestination(actionHeal);
		actionFindFood.addCondition(condTerminateFindFood);
		
		WarConditionSettings condSet3 = new WarConditionSettings();
		condSet3.Attribut_name = WarConditionAttributCheck.HEALTH;
		condSet3.Operateur = "<";
		condSet3.Reference = WarExplorer.MAX_HEALTH;
		condSet3.Pourcentage = 50;
		
		WarCondition<AgentAdapterType> condLowLife = 
				new WarConditionAttributCheck<AgentAdapterType>("cond_life", getBrain(), condSet3);
		condLowLife.setDestination(actionHeal);
		actionFindFood.addCondition(condLowLife);
		
		setFirstAction(actionHeal);
	}
	
}
