package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.FSM.WarFSMMessage;
import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.projectiles.WarRocket;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.brains.WarRocketLauncherBrain;
import edu.warbot.communications.WarMessage;
import edu.warbot.tools.CoordPolar;

public class WarActionDefendre extends WarAction{
	
	CoordPolar coordBase;
	
	public WarActionDefendre(WarBrain brain) {
		super(brain);
	}

	public String executeAction(){
		
		//si j'ai pas de missile et que je recharge pas, je recharge
		if(!getBrain().isReloaded() && !getBrain().isReloading()){
			return WarRocketLauncher.ACTION_RELOAD;
		}
		
		ArrayList<WarPercept> perceptEnemy = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);

		// si j'ai un enemy dans mon percept
		if(perceptEnemy != null & perceptEnemy.size() > 0){
			
			// si j'ai rechargé
			if(getBrain().isReloaded()){
				getBrain().setHeading(perceptEnemy.get(0).getAngle());
				return WarRocketLauncher.ACTION_FIRE;
				
			}else{// si j'ai pas rechargé
				if(perceptEnemy.get(0).getDistance() > WarRocket.EXPLOSION_RADIUS + WarRocketLauncher.SPEED + 1){
					getBrain().setHeading(perceptEnemy.get(0).getAngle());
					return MovableWarAgent.ACTION_MOVE;
				}else{
					return MovableWarAgent.ACTION_IDLE;
				}
				
			}
			
		}else{//Si j'ai pas d'enemy dans mon percept
			
			WarMessage m = getMessageFromBase();
			
			if(m != null)
				getBrain().setHeading(m.getAngle());
			
			return MovableWarAgent.ACTION_MOVE;

		}
		
	}
	
	private WarMessage getMessageFromBase() {
		for (WarMessage m : getBrain().getMessages()) {
			if(m.getSenderType().equals(WarAgentType.WarBase))
				return m;
		}
		
		getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, WarFSMMessage.whereAreYou, "");
		return null;
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
