package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionChercherNouriture;
import edu.warbot.FSM.action.WarActionRaporterNouriture;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionActionTerminate;
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
		
		WarAction<AgentAdapterType> actionChercheN = new WarActionChercherNouriture<AgentAdapterType>(getBrain(), 4);
		addAction(actionChercheN);

		WarAction<AgentAdapterType> actionRamenerN = new WarActionRaporterNouriture<AgentAdapterType>(getBrain(), 0);
		addAction(actionRamenerN);
		
		WarConditionSettings condSet1 = new WarConditionSettings();
		condSet1.Action = actionChercheN;
		WarCondition<AgentAdapterType> condStopChercher = new WarConditionActionTerminate<AgentAdapterType>("cond_back", getBrain(), condSet1);
		actionChercheN.addCondition(condStopChercher);
		condStopChercher.setDestination(actionRamenerN);
		
		WarConditionSettings condSet2 = new WarConditionSettings();
		condSet2.Action = actionRamenerN;
		WarCondition<AgentAdapterType> condStopRamener = new WarConditionActionTerminate<AgentAdapterType>("cond_seek", getBrain(), condSet2);
		actionRamenerN.addCondition(condStopRamener);
		condStopRamener.setDestination(actionChercheN);
		
		setFirstAction(actionChercheN);
	}
	
}
