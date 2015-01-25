package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.FSM.WarFSMMessage;
import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.brains.WarRocketLauncherBrain;
import edu.warbot.communications.WarMessage;
import edu.warbot.tools.CoordPolar;

/**
 * Cherche la base enemy
 * @author Olivier
 *
 */
public class WarActionChercherBase extends WarAction{
	
	CoordPolar coordPolarBase;
	
	public WarActionChercherBase(WarBrain brain) {
		super(brain);
	}

	@Override
	public String executeAction(){
		
		ArrayList<WarPercept> basePercepts = getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
		
		// Je vois la base
		if(basePercepts != null && basePercepts.size() > 0){ 
			
			String baseCoord[] = {String.valueOf(basePercepts.get(0).getAngle()), String.valueOf(basePercepts.get(0).getDistance()) };
			
			// Previent les autres unités de où est la base
			getBrain().broadcastMessageToAgentType(WarAgentType.WarRocketLauncher, WarFSMMessage.enemyBaseHere, baseCoord);

			setActionTerminate(true);
			
			getBrain().setHeading(basePercepts.get(0).getAngle());
			return MovableWarAgent.ACTION_MOVE;
			
		
		}else{ //Si il ny a pas de base dans le percept
			
			// Je me souvient d'ou est la base
			if(coordPolarBase != null && coordPolarBase.getDistance() > WarRocketLauncher.DISTANCE_OF_VIEW){
				getBrain().setHeading(coordPolarBase.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}else if(coordPolarBase != null && coordPolarBase.getDistance() < WarRocketLauncher.DISTANCE_OF_VIEW){
				this.coordPolarBase = null;
			}
			
			WarMessage m = getMessageLocateBase();
			
			//Si j'ai un message qui me dit où est la base
			if(m != null && m.getContent().length == 2){
				this.coordPolarBase = getBrain().getIndirectPositionOfAgentWithMessage(m);
				
				getBrain().setHeading(coordPolarBase.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}else if(m != null){
				getBrain().setHeading(m.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}
			
			if(getBrain().isBlocked())
				getBrain().setRandomHeading();
			
			return MovableWarAgent.ACTION_MOVE;
		}
		
	}

	private WarMessage getMessageLocateBase() {
		for (WarMessage m: getBrain().getMessages()) {
			if(m.getMessage().equals(WarFSMMessage.enemyBaseHere))
				return m;
		}
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
