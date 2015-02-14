package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionChercherNouriture;
import edu.warbot.FSM.action.WarActionRaporterNouriture;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionTimeOut;
import edu.warbot.FSMEditor.settings.EnumAction;
import edu.warbot.agents.enums.WarAgentType;
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
	
	public WarPlanRamasserNouriture(AgentAdapterType brain, WarPlanSettings planSettings) {
		super("Plan Rammaser Nouriture", brain, planSettings);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionChercheN = new WarActionChercherNouriture<AgentAdapterType>(getBrain());
		addAction(actionChercheN);

		WarAction<AgentAdapterType> actionRamenerN = new WarActionRaporterNouriture<AgentAdapterType>(getBrain());
		addAction(actionRamenerN);
		
		WarConditionSettings condSet1 = new WarConditionSettings();
		condSet1.Time_out = getPlanSettings().Time_out; //A changer par un attribut check
		WarCondition<AgentAdapterType> condStopChercher = new WarConditionTimeOut<AgentAdapterType>("cond_tO_R", getBrain(), condSet1);
		actionChercheN.addCondition(condStopChercher);
		condStopChercher.setDestination(actionRamenerN);
		
		WarConditionSettings condSet2 = new WarConditionSettings();
		condSet2.Time_out = getPlanSettings().Time_out; //A changer par un attribut check
		WarCondition<AgentAdapterType> condStopRamener = new WarConditionTimeOut<AgentAdapterType>("cond_tO_C", getBrain(), condSet2);
		actionRamenerN.addCondition(condStopRamener);
		condStopRamener.setDestination(actionChercheN);
		
		setFirstAction(actionChercheN);
	}
	
}
