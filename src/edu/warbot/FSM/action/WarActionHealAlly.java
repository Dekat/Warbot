package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.MovableWarAgentAdapter;

public class WarActionHealAlly<AgentAdapterType extends MovableWarAgentAdapter> extends WarAction<AgentAdapterType>{

	private WarAgentType agentType;

	public WarActionHealAlly(AgentAdapterType brain, WarAgentType agentType) {
		super(brain);
		this.agentType = agentType;
	}
	
	public String executeAction(){
		
		if(getAgent().isBagEmpty()){
			getAgent().setDebugString("ActionHealAlly : empty bag");
			return MovableWarAgent.ACTION_IDLE;
		}
		
		ArrayList<WarAgentPercept> percept = getAgent().getPerceptsAllies();
		
		for (WarAgentPercept p : percept) {
				
			if(p.getType().equals(agentType) && 
					p.getHealth() + WarFood.HEALTH_GIVEN < p.getMaxHealth() ){

				if(p.getDistance() < MovableWarAgent.MAX_DISTANCE_GIVE){
					getAgent().setDebugString("ActionHealAlly : ally healed" + p.getType());
					getAgent().setIdNextAgentToGive(p.getID());
					return MovableWarAgent.ACTION_GIVE;
				}else{
					getAgent().setDebugString("ActionHealAlly : heal ally " + p.getType());
					getAgent().setHeading(p.getAngle());
					return MovableWarAgent.ACTION_MOVE;
				}
				
			}
		}
			
		if(getAgent().isBlocked())
			getAgent().setRandomHeading();
		
		getAgent().setDebugString("ActionHealAlly : seek ally");
		return MovableWarAgent.ACTION_MOVE;
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
}
