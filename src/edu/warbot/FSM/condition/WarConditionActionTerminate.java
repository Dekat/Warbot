package edu.warbot.FSM.condition;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.brains.WarBrain;

public class WarConditionActionTerminate extends WarCondition{
	
	WarAction action;
	
	public WarConditionActionTerminate(WarBrain brain, WarAction action){
		super(brain);
		this.action = action;
	}

	@Override
	public boolean isValide() {
		if(this.action.isTerminate())
			return true;
		else
			return false;
			
	}

}
