package edu.warbot.FSM.plan;

import javax.swing.JOptionPane;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionChercherNouriture;
import edu.warbot.FSM.action.WarActionHealAlly;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionTimeOut;
import edu.warbot.FSMEditor.settings.GenericConditionSettings;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Heal les alliés
 */
public class WarPlanHealAlly<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	private WarAgentType agentType;

	public WarPlanHealAlly(AgentAdapterType brain, GenericPlanSettings planSettings) {
		super("Plan heal ally", brain, planSettings);
		
		if(getPlanSettings().Agent_type != null)
			this.agentType = getPlanSettings().Agent_type;
		else
			JOptionPane.showMessageDialog(null, "You must chose <Agent_type> for plan <WarPlanHealAlly>", "Missing attribut", JOptionPane.ERROR_MESSAGE);
		
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionHeal = 
				new WarActionHealAlly<>(getBrain(), agentType);
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
