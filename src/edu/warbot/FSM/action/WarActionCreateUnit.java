package edu.warbot.FSM.action;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarBaseAdapter;

/**
 * Description de l'action
 * Si ma vie est inferieur au pourcentage passé en parametre je me heal 
 * si ma vie est supperieur au pourentage passé en parametre j'ai finit
 * Idem pour constructeur avec deux pourcentages (le deuxieme pour les unités alliées)
 * Si plus de nourriure je finit
 * @author Olivier
 *
 */
public class WarActionCreateUnit extends WarAction<WarBaseAdapter> {

	int minLife;
	WarAgentType agentType;
	int nbToCreate;
	int nbCreate;
	
	public WarActionCreateUnit(WarBaseAdapter brain, WarAgentType agentType, int nb, int minLife) {
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
		
		if(!getAgent().isBagEmpty() && getAgent().getHealth() < this.minLife)
			return WarBase.ACTION_EAT;
		
		if(getAgent().getHealth() >= minLife){
			getAgent().setNextAgentToCreate(agentType);
			nbCreate++;
			return WarBase.ACTION_CREATE;
		}
		
		return WarBase.ACTION_IDLE;
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
		getAgent().setDebugString(getClass().getSimpleName() + " " + agentType.toString());
		this.nbCreate = 0;
	}
	
}
