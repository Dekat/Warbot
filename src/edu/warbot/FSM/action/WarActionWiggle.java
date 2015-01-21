package edu.warbot.FSM.action;

import java.util.Random;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.brains.WarBrain;

/**
 * Description de l'action
 * @author Olivier
 *
 */
public class WarActionWiggle extends WarAction{

	private final int nbPasMax;	
	int nbPas = 0;
	
	public WarActionWiggle(WarBrain brain, int nombrePas) {
		super(brain);
		this.nbPasMax = nombrePas;
	}
	
	public String executeAction(){
		
		if(this.nbPasMax == 0)
			return MovableWarAgent.ACTION_MOVE;
		
		nbPas++;
		
		if(this.nbPas > nbPasMax){
			setActionTerminate(true);
			return MovableWarAgent.ACTION_IDLE;
		}
		
		double angle = getBrain().getHeading();
		
		angle = angle + new Random().nextInt(30) - new Random().nextInt(30);
		
		getBrain().setHeading(angle);
	
		return MovableWarAgent.ACTION_MOVE;

		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
		nbPas = 0;
	}

}
