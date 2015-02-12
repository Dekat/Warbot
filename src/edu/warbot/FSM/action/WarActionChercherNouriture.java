package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.communications.WarMessage;

/**
 * Va chercher de la nouriture
 * @author Olivier
 *
 */
public class WarActionChercherNouriture<AgentAdapterType extends MovableWarAgentAdapter> extends WarAction<AgentAdapterType> {
	
	private final int nbMaxElement;	

	public WarActionChercherNouriture(AgentAdapterType brain, int nombreNuritureMax) {
		super(brain);
		this.nbMaxElement = nombreNuritureMax;
	}

	public String executeAction(){
		
//		if(getAgent().isBagFull() | getAgent().getNbElementsInBag() >=this.nbMaxElement)
//			setActionTerminate(true);
		
		if(getAgent().isBlocked())
			getAgent().setRandomHeading();

		ArrayList<WarPercept> foodPercepts = getAgent().getPerceptsResources();
		
		//Si il ny a pas de nouriture dans le percept
		if(foodPercepts.size() == 0){
			
			WarMessage m = getMessageAboutFood();
			if(m != null){
				getAgent().setHeading(m.getAngle());
			}
			
			//Sinon je vais tout droit
			return MovableWarAgent.ACTION_MOVE;
				
		}else{//Si il y a de la nouriture
			WarPercept foodP = foodPercepts.get(0); //le 0 est le plus proche normalement
			
			//si il y a beaucoup de nourriture je previens mes alliés
			if(foodPercepts.size() > 1)
				getAgent().broadcastMessageToAgentType(WarAgentType.WarExplorer, "foodHere", "");
			
			if(foodP.getDistance() > ControllableWarAgent.MAX_DISTANCE_GIVE){
				getAgent().setHeading(foodP.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}else{
				return MovableWarAgent.ACTION_TAKE;
			}
		}
		
	}

	private WarMessage getMessageAboutFood() {
		for (WarMessage m : getAgent().getMessages()) {
			if(m.getMessage().equals("foodHere"))
				return m;
		}
		return null;
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
		getAgent().setHeading(getAgent().getHeading() + 180);
	}

}
