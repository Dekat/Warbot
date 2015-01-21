package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.MovableWarAgentBrain;
import edu.warbot.brains.WarBrain;

/**
 * Description de l'action
 * @author Olivier
 *
 */
public class WarActionFuire extends WarAction{
	
	private int lifeBeforStopFuire;

	public WarActionFuire(WarBrain brain, int lifeBeforStopFuire, int pourcentage) {
		super(brain, "ActionFuire");
		this.lifeBeforStopFuire = lifeBeforStopFuire * pourcentage/100;
	}
	
	public String executeAction(){
		
		if(getBrain().isBlocked())
			getBrain().setRandomHeading();
		
		if(getBrain().getHealth() >= this.lifeBeforStopFuire)
			setActionTerminate(true);
		
		//Si je n'ai pas denemie autour de moi j'ai termin�
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		
		// si il y a des enemy je fuis
		if(percept.size() > 0){
			getBrain().setHeading(percept.get(0).getAngle() + 180);
			return MovableWarAgent.ACTION_MOVE;
		}else{
			// Sinon je prend de la nouriture pour me soigner
			percept = getBrain().getPerceptsResources();
			if(percept.size() > 0){
				WarPercept p = percept.get(0);
				if(p.getDistance() < WarFood.MAX_DISTANCE_TAKE)
					return MovableWarAgent.ACTION_TAKE;
				else{
					getBrain().setHeading(p.getAngle());
					return MovableWarAgent.ACTION_MOVE;
				}
			}else{
				if(!getBrain().isBagEmpty()) // si j'ai de la nourriture je mange sinon je vais tout droit
					return MovableWarAgent.ACTION_EAT;
				else
					return MovableWarAgent.ACTION_MOVE;
			}
		}
		
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
	

	@Override
	public MovableWarAgentBrain getBrain(){
		return (MovableWarAgentBrain)(super.getBrain());
	}

}
