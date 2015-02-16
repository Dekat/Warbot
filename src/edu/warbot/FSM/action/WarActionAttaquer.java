package edu.warbot.FSM.action;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;
import edu.warbot.tools.geometry.CoordPolar;

import java.util.ArrayList;

public class WarActionAttaquer extends WarAction<WarRocketLauncherAdapter> {
	
	CoordPolar coordBase;
	WarAgentType agentType;
	
	public WarActionAttaquer(WarRocketLauncherAdapter agentAdapter, WarAgentType agentType) {
		super(agentAdapter);
		this.agentType = agentType;
	}

	public String executeAction(){
		
		if(!getAgent().isReloaded() && !getAgent().isReloading()){
			getAgent().setDebugString("ActionAttaquer : reloading");
			return WarRocketLauncher.ACTION_RELOAD;
		}
		
		ArrayList<WarAgentPercept> percept = getAgent().getPerceptsEnemiesByType(this.agentType);
		
		// Je un agentType dans le percept
		if(percept != null && percept.size() > 0){
			
			getAgent().setHeading(percept.get(0).getAngle());

			if(getAgent().isReloaded()){
				
				getAgent().setDebugString("ActionAttaquer : fire");
				return WarRocketLauncher.ACTION_FIRE;
			}else{
				//placement mieux
				
				//en attendant 
				getAgent().setDebugString("ActionAttaquer : waiting to reaload");
				if(percept.get(0).getDistance() > 10)
					return WarRocketLauncher.ACTION_MOVE;
				else
					return WarRocketLauncher.ACTION_IDLE;
			}
		}else{ //Si il ny a pas agentType dans le percept

			if(getAgent().isBlocked())
				getAgent().setRandomHeading();
			getAgent().setDebugString("ActionAttaquer : seek enemy");
			return MovableWarAgent.ACTION_MOVE;

		}
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
	
}
