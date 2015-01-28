package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionCreateUnit;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionActionTerminate;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarBaseAdapter;

/**
 * Desciption du plan et de ces actions
 * ATTENTION ce plan est basé sur la size bag d'un explorer et la la vie max d'un explorer
 * @author Olivier
 *
 */
public class WarPlanCreateUnit extends WarPlan<WarBaseAdapter>{
	
	
	int nombreAgent[];
	WarAgentType agentType[];
	
	WarAction<WarBaseAdapter> actions[];
	WarCondition<WarBaseAdapter> cond[];
	
	int minLife;
	
	@SuppressWarnings("unchecked")
	public WarPlanCreateUnit(WarBaseAdapter brain, WarAgentType agentTypes[], int nombreAgent[], int reference, int pourcentage) {
		super(brain, "Plan healer");	
		this.nombreAgent = nombreAgent;
		this.agentType = agentTypes;
		
		actions = new WarAction[nombreAgent.length];
		cond = new WarCondition[nombreAgent.length];
		
		this.minLife = reference*pourcentage/100;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		int nbA = nombreAgent.length;
		
		//cree les actions
		for(int i = 0; i < nbA; i++){
			this.actions[i] = new WarActionCreateUnit(getBrain(), agentType[i], nombreAgent[i], minLife);
			addAction(actions[i]);
		}
		
		//Cree les conditions si plus de une action
		if(nbA > 1){
			for(int i = 0; i < nbA; i++){
				
				cond[i%nbA] = new WarConditionActionTerminate<WarBaseAdapter>(getBrain(), actions[i%nbA]);
				cond[i%nbA].setDestination(actions[(i+1)%nbA]);
				actions[i%nbA].addCondition(cond[i%nbA]);
			}
		}
		
		setFirstAction(this.actions[0]);
	}
	
}
