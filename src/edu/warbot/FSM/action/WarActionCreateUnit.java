package edu.warbot.FSM.action;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.brains.WarBaseBrain;

/**
 * Description de l'action
 * Si ma vie est inferieur au pourcentage pass� en parametre je me heal 
 * si ma vie est supperieur au pourentage pass� en parametre j'ai finit
 * Idem pour constructeur avec deux pourcentages (le deuxieme pour les unit� alli�)
 * Si plus de nourriure je finit
 * @author Olivier
 *
 */
public class WarActionCreateUnit extends WarAction{

	int minLife;
	WarAgentType agentType;
	int nbToCreate;
	int nbCreate;
	
	public WarActionCreateUnit(WarBrain brain, WarAgentType agentType, int nb, int minLife) {
		super(brain);
		this.agentType = agentType;
		this.nbToCreate = nb;
		this.minLife = minLife;
	}

	public String executeAction(){
		
		if(nbCreate == nbToCreate){
			setActionTerminate(true);
			return MovableWarAgent.ACTION_IDLE;
		}
		
		if(!getBrain().isBagEmpty() && getBrain().getHealth() < this.minLife)
			return WarBase.ACTION_EAT;
		
		if(getBrain().getHealth() >= minLife){
			getBrain().setNextAgentToCreate(agentType);
			nbCreate++;
			return WarBase.ACTION_CREATE;
		}
		
		return WarBase.ACTION_IDLE;
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
		getBrain().setDebugString(getClass().getSimpleName() + " " + agentType.toString());
		this.nbCreate = 0;
	}
	
	@Override
	public WarBaseBrain getBrain(){
		return (WarBaseBrain)(super.getBrain());
	}

}
