package edu.warbot.FSM.condition;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.brains.ControllableWarAgentAdapter;

public abstract class WarCondition<AgentAdapterType extends ControllableWarAgentAdapter> {
	
	AgentAdapterType brain;
	
	private WarEtat<AgentAdapterType> etatDestination;
	private WarAction<AgentAdapterType> actionDestination;
	
	public WarCondition(AgentAdapterType b){
		this.brain = b;
	}
	
	public abstract boolean isValide();

	public void setDestination(WarEtat<AgentAdapterType> etatDestination) {
		this.etatDestination = etatDestination;
	}
	
	public void setDestination(WarAction<AgentAdapterType> a) {
		this.actionDestination = a;
	}

	public WarEtat<AgentAdapterType> getEtatDestination() {
		return etatDestination;
	}

	public void init() {
		if(this.etatDestination == null & this.actionDestination == null){
			System.err.println("ERREUR une condition doit obligatoirement avoir un état ou une action destination <" + this.toString() + ">");
			System.exit(0);
		}
		
		if(this.etatDestination == null){
			System.out.println("ATTENTION la condition <" + this.getClass() + "> ne contient pas d'état de sortie. Par default l'action appelée sera celle de l'état courant");
		}
		
		
	}

	public WarAction<AgentAdapterType> getActionDestination() {
		return this.actionDestination;
	}
	
	public AgentAdapterType getBrain(){
		return this.brain;
	}
	
	
	public static final String HEALTH = "getHealth";
	public static final String NB_ELEMEN_IN_BAG = "getNbElementsInBag";

}
