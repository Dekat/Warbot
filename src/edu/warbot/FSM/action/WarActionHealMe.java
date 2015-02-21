package edu.warbot.FSM.action;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.MovableWarAgentAdapter;

public class WarActionHealMe<AgentAdapterType extends ControllableWarAgentAdapter> extends WarAction<AgentAdapterType>{

	public WarActionHealMe(AgentAdapterType brain) {
		super(brain);
	}
	
	public String executeAction(){
		
		if(getAgent().isBagEmpty()){
			getAgent().setDebugString("ActionHealMe : empty bag");
			return MovableWarAgent.ACTION_IDLE;
		}
		
		if(! getAgent().isFullLife()){
			getAgent().setDebugString("ActionHealMe : eat");
			return MovableWarAgent.ACTION_EAT;
			
		}else{
			getAgent().setDebugString("ActionHealMe : full life");
			return MovableWarAgent.ACTION_IDLE;
		}
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
}
