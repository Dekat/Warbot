package edu.warbot.FSM.action;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarBaseAdapter;

/**
 * Crée le nombre d'unité passé en parametre si ma vie est supperieure a la valeur passé en parametre
 * Crée de agent du type passé en parametre
 * @author Olivier
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
