package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.ConditionSettings;
import edu.warbot.FSM.WarGenericSettings.PlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionChercherNouriture;
import edu.warbot.FSM.action.WarActionRaporterNouriture;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionBooleanCheck;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Desciption du plan et de ces actions
 * action cercher nouriture
 * Si j'ai finit => raporter la nouriture (ajouter si le sac est plein comme condition)
 * action raporter de la nouriture
 * si j'ai finit => action chercher de la nouriture	(ajouter si le sac est vide comme condition)
 *
 */
public class WarPlanRamasserNouriture<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	public WarPlanRamasserNouriture(AgentAdapterType brain, PlanSettings planSettings) {
		super("Plan Rammaser Nouriture", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionChercheN = new WarActionChercherNouriture<AgentAdapterType>(getBrain());
		addAction(actionChercheN);

		WarAction<AgentAdapterType> actionRamenerN = new WarActionRaporterNouriture<AgentAdapterType>(getBrain());
		addAction(actionRamenerN);
		
		ConditionSettings condSet1 = new ConditionSettings();
		condSet1.Methode = EnumMethod.isBagFull;
		WarCondition<AgentAdapterType> condStopChercher = new WarConditionBooleanCheck<AgentAdapterType>("cond_tO_R", getBrain(), condSet1);
		actionChercheN.addCondition(condStopChercher);
		condStopChercher.setDestination(actionRamenerN);
		
		ConditionSettings condSet2 = new ConditionSettings();
		condSet2.Methode = EnumMethod.isBagEmpty;
		WarCondition<AgentAdapterType> condStopRamener = new WarConditionBooleanCheck<AgentAdapterType>("cond_tO_C", getBrain(), condSet2);
		actionRamenerN.addCondition(condStopRamener);
		condStopRamener.setDestination(actionChercheN);
		
		setFirstAction(actionChercheN);
	}
	
}
