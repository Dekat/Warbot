package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;
import edu.warbot.tools.CoordPolar;

/**
 * @author Olivier
 */
public class WarActionAttaquer extends WarAction<WarRocketLauncherAdapter> {
	
	CoordPolar coordBase;
	WarAgentType agentType;
	
	public WarActionAttaquer(WarRocketLauncherAdapter agentAdapter, WarAgentType agentType) {
		super(agentAdapter);
		this.agentType = agentType;
	}

	public String executeAction(){
		
		if(!getAgent().isReloaded() && !getAgent().isReloading()){
			return WarRocketLauncher.ACTION_RELOAD;
		}
		
		ArrayList<WarAgentPercept> percept = getAgent().getPerceptsEnemiesByType(this.agentType);
		
		// Je un agentType dans le percept
		if(percept != null && percept.size() > 0){
			
			if(getAgent().isReloaded()){
				
				getAgent().setHeading(percept.get(0).getAngle());
				return WarRocketLauncher.ACTION_FIRE;
			}else{
				//placement mieux
				
				//en attendant 
				return WarRocketLauncher.ACTION_IDLE;
			}
		}else{ //Si il ny a pas agentType dans le percept

			return MovableWarAgent.ACTION_IDLE;

		}
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
	
}
