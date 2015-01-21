package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.MovableWarAgentBrain;

/**
 * @author Olivier
 *
 */
public class WarPlanAttaquer extends WarPlan{
	
	WarAgentType agentType;
	
	public WarPlanAttaquer(MovableWarAgentBrain brain, WarAgentType agentType) {
		super(brain, "Plan Attaquer");
		this.agentType = agentType;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction actionAttaquer = new WarActionAttaquer(getBrain(), this.agentType);
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
	public MovableWarAgentBrain getBrain(){
		return (MovableWarAgentBrain)super.getBrain();
	}
	
}
