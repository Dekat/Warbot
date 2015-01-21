package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionFuire;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.brains.MovableWarAgentBrain;

/**
 * Desciption du plan et de ces actions
 * action ne pas bouger
 * Condition de sortie si j'ai perdu la moiti� de ma vie.
 * condition de sortie si l'action est termin� alors je la recommence (debug)
 * retourne dans pas bouger si j'ai plus de la moiti� de ma vie
 * @author Olivier
 *
 */
public class WarPlanBeSecure extends WarPlan{
	
	public WarPlanBeSecure(MovableWarAgentBrain brain) {
		super(brain, "PlanBeSecure");
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction actionFuire = new WarActionFuire(getBrain(), WarExplorer.MAX_HEALTH, 100);
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
	
	public MovableWarAgentBrain getBrain(){
		return (MovableWarAgentBrain)super.getBrain();
	}
	
}
