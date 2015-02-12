package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.communications.WarMessage;

/**
 * Raporte la nouriture
 *
 */
public class WarActionRaporterNouriture<AgentAdapterType extends MovableWarAgentAdapter> extends WarAction<AgentAdapterType>{
	
	private final int nbElementToKeep;	

	public WarActionRaporterNouriture(AgentAdapterType brain, int nombreAGarder) {
		super(brain);
		this.nbElementToKeep = nombreAGarder;
	}

	public String executeAction(){
		
//		if(getAgent().isBagEmpty() | getAgent().getNbElementsInBag() <= nbElementToKeep)
//			setActionTerminate(true);
		
		if(getAgent().isBlocked())
			getAgent().setRandomHeading();

		ArrayList<WarPercept> basePercepts = getAgent().getPerceptsAlliesByType(WarAgentType.WarBase);
		
		//Si je ne voit pas de base vois une base
		if(basePercepts == null | basePercepts.size() == 0){
			
			WarMessage m = this.getMessageFromBase();
			//Si j'ai un message de la base je vais vers elle
			if(m != null)
				getAgent().setHeading(m.getAngle());
			
			//j'envoi un message a la base pour savoir où elle est
			//getBrain().broadcastMessageDefaultRole(WarBase., "whereAreYou", null);
			
			return MovableWarAgent.ACTION_MOVE;
			
		}else{//si je vois une base
			WarPercept base = basePercepts.get(0);
			
			if(base.getDistance() > MovableWarAgent.MAX_DISTANCE_GIVE){
				getAgent().setHeading(base.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}else{
				getAgent().setIdNextAgentToGive(base.getID());
				return MovableWarAgent.ACTION_GIVE;
			}
			
		}
		
	}
	
	private WarMessage getMessageFromBase() {
		for (WarMessage m : getAgent().getMessages()) {
			if(m.getSenderType().equals(WarAgentType.WarBase))
				return m;
		}
		
		getAgent().broadcastMessageToAgentType(WarAgentType.WarBase, "whereAreYou", "");
		return null;
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
}
