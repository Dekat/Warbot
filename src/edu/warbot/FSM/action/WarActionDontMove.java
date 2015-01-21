package edu.warbot.FSM.action;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.brains.WarBrain;

/**
 * Ne bouge pas pendant un certains nombre de pas (tik)
 * @author Olivier
 *
 */
public class WarActionDontMove extends WarAction{
	
	private final int nbPasMax;	
	int nbPas = 0;
	
	public WarActionDontMove(WarBrain brain, int nombrePas) {
		super(brain);
		this.nbPasMax = nombrePas;
	}

	
	public String executeAction(){
		
		if(this.nbPasMax == 0)
			return MovableWarAgent.ACTION_MOVE;
		
		nbPas++;
		
		//System.out.println("J'execute l'action dont move je suis au pas " + nbPas);
		if(this.nbPas > nbPasMax){
			setActionTerminate(true);
			return MovableWarAgent.ACTION_IDLE;
		}
		
		if(nbPas < nbPasMax)
			return MovableWarAgent.ACTION_IDLE;
		else
			return MovableWarAgent.ACTION_MOVE;
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
		nbPas = 0;
		System.out.println("Je vais commencer l'action " + this.getClass().getSimpleName());
	}

}
