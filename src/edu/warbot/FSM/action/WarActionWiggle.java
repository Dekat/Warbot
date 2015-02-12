package edu.warbot.FSM.action;

import java.util.Random;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Description de l'action
 * @author Olivier
 *
 */
public class WarActionWiggle<AgentAdapterType extends MovableWarAgentAdapter> extends WarAction<AgentAdapterType> {

	private final int nbPasMax;	
	int nbPas = 0;
	
	public WarActionWiggle(AgentAdapterType brain, int nombrePas) {
		super(brain);
		this.nbPasMax = nombrePas;
	}
	
	public String executeAction(){
		
		if(this.nbPasMax == 0)
			return MovableWarAgent.ACTION_MOVE;
		
		nbPas++;
		
		if(this.nbPas > nbPasMax){
			return MovableWarAgent.ACTION_IDLE;
		}
		
		double angle = getAgent().getHeading();
		
		angle = angle + new Random().nextInt(30) - new Random().nextInt(30);
		
		getAgent().setHeading(angle);
	
		return MovableWarAgent.ACTION_MOVE;

		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
		nbPas = 0;
	}

}
