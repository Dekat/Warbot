package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionDefendre;
import edu.warbot.brains.MovableWarAgentBrain;

/**
 * @author Olivier
 *
 */
public class WarPlanDefendre extends WarPlan{
	
	public WarPlanDefendre(MovableWarAgentBrain brain) {
		super(brain, "Plan Defendre");
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction actionDef = new WarActionDefendre(getBrain());
		addAction(actionDef);
		
		setFirstAction(actionDef);
	}
	
	public MovableWarAgentBrain getBrain(){
		return (MovableWarAgentBrain)super.getBrain();
	}
	
}
