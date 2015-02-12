package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Description de l'action
 * @author Olivier
 *
 */
public class WarActionFuire<AgentAdapterType extends MovableWarAgentAdapter> extends WarAction<AgentAdapterType>{
	
	private int lifeBeforStopFuire;

	public WarActionFuire(AgentAdapterType brain, int lifeBeforStopFuire, int pourcentage) {
		super(brain, "ActionFuire");
		this.lifeBeforStopFuire = lifeBeforStopFuire * pourcentage/100;
	}
	
	public String executeAction(){
		
		if(getAgent().isBlocked())
			getAgent().setRandomHeading();
		
//		if(getAgent().getHealth() >= this.lifeBeforStopFuire)
//			setActionTerminate(true);
		
		//Si je n'ai pas denemie autour de moi j'ai terminé
		ArrayList<WarAgentPercept> percept = getAgent().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		
		// si il y a des enemy je fuis
		if(percept.size() > 0){
			getAgent().setHeading(percept.get(0).getAngle() + 180);
			return MovableWarAgent.ACTION_MOVE;
		}else{
			// Sinon je prend de la nouriture pour me soigner
			percept = getAgent().getPerceptsResources();
			if(percept.size() > 0){
                WarAgentPercept p = percept.get(0);
				if(p.getDistance() < WarFood.MAX_DISTANCE_TAKE)
					return MovableWarAgent.ACTION_TAKE;
				else{
					getAgent().setHeading(p.getAngle());
					return MovableWarAgent.ACTION_MOVE;
				}
			}else{
				if(!getAgent().isBagEmpty()) // si j'ai de la nourriture je mange sinon je vais tout droit
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
	
}
