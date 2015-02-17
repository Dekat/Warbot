package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionChercherNouriture;
import edu.warbot.FSM.action.WarActionHealAlly;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionTimeOut;
import edu.warbot.FSMEditor.settings.GenericConditionSettings;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Heal les alliés
 *
 */
public class WarPlanHealAlly<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	public WarPlanHealAlly(AgentAdapterType brain, GenericPlanSettings planSettings) {
		super("Plan heal ally", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionHeal = 
				new WarActionHealAlly<>(getBrain(), getPlanSettings().Agent_type);
		addAction(actionHeal);
		
		WarAction<AgentAdapterType> actionFindFood = new WarActionChercherNouriture<>(getBrain());
		addAction(actionFindFood);
		
		GenericConditionSettings condSet1 = new GenericConditionSettings();
		condSet1.Methode = EnumMethod.isBagEmpty; 
		WarCondition<AgentAdapterType> condhealToFind = 
				new WarConditionTimeOut<>("cond_heal", getBrain(), condSet1);
		actionHeal.addCondition(condhealToFind);
		condhealToFind.setDestination(actionFindFood);
		
		GenericConditionSettings condSet2 = new GenericConditionSettings();
		condSet2.Methode = EnumMethod.isBagFull;
		WarCondition<AgentAdapterType> condFindToHeal = 
				new WarConditionTimeOut<>("cond_food", getBrain(), condSet2);
		condFindToHeal.setDestination(actionHeal);
		actionFindFood.addCondition(condFindToHeal);
		
		setFirstAction(actionHeal);
	}
	
}
