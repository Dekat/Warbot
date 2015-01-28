package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionChercherNouriture;
import edu.warbot.FSM.action.WarActionRaporterNouriture;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionActionTerminate;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Desciption du plan et de ces actions
 * action cercher nouriture
 * Si j'ai finit => raporter la nouriture	(ajouter si le sac est plaein comme condition)
 * action raporter de la nouriture
 * si j'ai finit => action chercher de la nouriture	(ajouter si le sac est vide comme condition)
 * @author Olivier
 *
 */
public class WarPlanRamasserNouriture<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	public WarPlanRamasserNouriture(AgentAdapterType brain) {
		super(brain, "PlanRammaserNouriture");
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionChercheN = new WarActionChercherNouriture<AgentAdapterType>(getBrain(), 4);
		addAction(actionChercheN);

		WarAction<AgentAdapterType> actionRamenerN = new WarActionRaporterNouriture<AgentAdapterType>(getBrain(), 0);
		addAction(actionRamenerN);
		
		WarCondition<AgentAdapterType> condStopChercher = new WarConditionActionTerminate<AgentAdapterType>(getBrain(), actionChercheN);
		actionChercheN.addCondition(condStopChercher);
		condStopChercher.setDestination(actionRamenerN);
		
		WarCondition<AgentAdapterType> condStopRamener = new WarConditionActionTerminate<AgentAdapterType>(getBrain(), actionRamenerN);
		actionRamenerN.addCondition(condStopRamener);
		condStopRamener.setDestination(actionChercheN);
		
		setFirstAction(actionChercheN);
	}
	
}
