package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.brains.WarRocketLauncherBrain;
import edu.warbot.tools.CoordPolar;

/**
 * @author Olivier
 */
public class WarActionAttaquer extends WarAction{
	
	CoordPolar coordBase;
	WarAgentType agentType;
	
	public WarActionAttaquer(WarBrain brain, WarAgentType agentType) {
		super(brain);
		this.agentType = agentType;
	}

	public String executeAction(){
		
		if(!getBrain().isReloaded() && !getBrain().isReloading()){
			return WarRocketLauncher.ACTION_RELOAD;
		}
		
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(this.agentType);
		
		// Je un agentType dans le percept
		if(percept != null && percept.size() > 0){
			
			if(getBrain().isReloaded()){
				
				getBrain().setHeading(percept.get(0).getAngle());
				return WarRocketLauncher.ACTION_FIRE;
			}else{
				//placement mieux
				
				//en attendant 
				return WarRocketLauncher.ACTION_IDLE;
			}
		}else{ //Si il ny a pas agentType dans le percept
			setActionTerminate(true);

			return MovableWarAgent.ACTION_IDLE;

		}
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
	
	@Override
	public WarRocketLauncherBrain getBrain(){
		return (WarRocketLauncherBrain)(super.getBrain());
	}

}
