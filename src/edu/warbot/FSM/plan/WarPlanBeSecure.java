package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionFuire;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Desciption du plan et de ces actions
 * action ne pas bouger
 * Condition de sortie si j'ai perdu la moitié de ma vie.
 * condition de sortie si l'action est terminé alors je la recommence (debug)
 * retourne dans pas bouger si j'ai plus de la moitié de ma vie
 * @author Olivier
 *
 */
public class WarPlanBeSecure<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	public WarPlanBeSecure(AgentAdapterType brain) {
		super(brain, "PlanBeSecure");
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionFuire = new WarActionFuire<AgentAdapterType>(getBrain(), WarExplorer.MAX_HEALTH, 100);
		addAction(actionFuire);

		/*
		WarAction actionDontMove = new WarActionDontMove(getBrain(), 1000);
		addAction(actionDontMove);
		
		WarCondition condFinitDontMove = new WarConditionActionTerminate(getBrain(), actionDontMove);
		actionDontMove.addCondition(condFinitDontMove);
		condFinitDontMove.setDestination(actionDontMove);
		
		WarCondition condBegginFuire = new WarConditionAttributCheck(getBrain(), WarConditionAttributCheck.HEALTH, "<", WarExplorer.MAX_HEALTH, 50);
		actionDontMove.addCondition(condBegginFuire);
		condBegginFuire.setDestination(actionFuire);
		
		WarCondition condStopFuire = new WarConditionAttributCheck(getBrain(), WarConditionAttributCheck.HEALTH, ">", WarExplorer.MAX_HEALTH, 70);
		actionFuire.addCondition(condStopFuire);
		condStopFuire.setDestination(actionDontMove);
		 */
		setFirstAction(actionFuire);
	}
	
}
