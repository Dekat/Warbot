package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.communications.WarMessage;

/**
 * Raporte la nouriture
 *
 */
public class WarActionRaporterNouriture<AgentAdapterType extends MovableWarAgentAdapter> extends WarAction<AgentAdapterType>{
	
	public WarActionRaporterNouriture(AgentAdapterType brain) {
		super(brain);
	}

	public String executeAction(){
		
		if(getAgent().isBagEmpty()){
			getAgent().setDebugString("Action RapporterNourriture : bag empty");
			if(getAgent().isBlocked())
				getAgent().setRandomHeading();
			return MovableWarAgent.ACTION_MOVE;
		}
		
		if(getAgent().isBlocked())
			getAgent().setRandomHeading();

		ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();

		ArrayList<WarAgentPercept> basePercepts = new ArrayList<>();
		
		for (WarAgentPercept p : percepts) {
			if(p.getType().equals(WarAgentType.WarBase))
				basePercepts.add(p);
		}
		
		//Si je ne voit pas de base
		if(basePercepts == null | basePercepts.size() == 0){
			
			getAgent().setDebugString("Action RapporterNourriture : seek base");
			
			WarMessage m = this.getMessageFromBase();
			//Si j'ai un message de la base je vais vers elle
			if(m != null)
				getAgent().setHeading(m.getAngle());
			
			//j'envoi un message a la base pour savoir où elle est
			//getBrain().broadcastMessageDefaultRole(WarBase., "whereAreYou", null);
			
			return MovableWarAgent.ACTION_MOVE;
			
		}else{//si je vois une base
			
			getAgent().setDebugString("Action RapporterNourriture : base found");
			
            WarAgentPercept base = basePercepts.get(0);
			
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
