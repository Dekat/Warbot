package edu.warbot.FSM.condition;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.brains.WarBrain;

public abstract class WarCondition {
	
	WarBrain brain;
	
	private WarEtat etatDestination;
	private WarAction actionDestination;
	
	public WarCondition(WarBrain b){
		this.brain = b;
	}
	
	public abstract boolean isValide();

	public void setDestination(WarEtat etatDestination) {
		this.etatDestination = etatDestination;
	}
	
	public void setDestination(WarAction a) {
		this.actionDestination = a;
	}

	public WarEtat getEtatDestination() {
		return etatDestination;
	}

	public void init() {
		if(this.etatDestination == null & this.actionDestination == null){
			System.err.println("ERREUR une condition doit obligatoirement avoir un �tat ou une action destination <" + this.toString() + ">");
			System.exit(0);
		}
		
		if(this.etatDestination == null){
			System.out.println("ATTENTION la condition <" + this.getClass() + "> ne contient pas d'�tat de sortie. Par default l'action appel� sera celle de l'�tat courant");
		}
		
		
	}

	public WarAction getActionDestination() {
		return this.actionDestination;
	}
	
	public WarBrain getBrain(){
		return this.brain;
	}
	
	
	public static final String HEALTH = "getHealth";
	public static final String NB_ELEMEN_IN_BAG = "getNbElementsInBag";

}
