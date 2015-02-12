package edu.warbot.FSM.plan;

import java.util.ArrayList;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionCreateUnit;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionTimeOut;
import edu.warbot.FSMEditor.settings.EnumAction;
import edu.warbot.FSMEditor.settings.Settings;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarBaseAdapter;

/**
 * Desciption du plan et de ces actions
 * ATTENTION ce plan est bas� sur la size bag d'un explorer et la vie max d'un explorer
 */
public class WarPlanCreateUnit extends WarPlan<WarBaseAdapter>{
	
	Integer nombreAgent[];
	WarAgentType[] agentType;
	
	WarAction<WarBaseAdapter> actions[];
	WarCondition<WarBaseAdapter> cond[];
	
	int minLife;
	int reference;
	int pourcentage;
	
	//WarAgentType agentTypes[], int nombreAgent[], int reference, int pourcentage
	@SuppressWarnings("unchecked")
	public WarPlanCreateUnit(WarBaseAdapter brain, WarPlanSettings planSettings ) {
		super("Plan healer", brain, planSettings);
		
		this.agentType = getPlanSettings().Agent_type;
		this.nombreAgent = getPlanSettings().Number_agent;
		
		this.reference = getPlanSettings().Value_reference;
		this.pourcentage = getPlanSettings().Value_pourcentage;
		
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
				
				WarConditionSettings condSet1 = new WarConditionSettings();
				condSet1.Action = actions[i%nbA].getType(); //EnumAction.valueOf(actions[i%nbA].getName());
				cond[i%nbA] = new WarConditionTimeOut<WarBaseAdapter>("cond_"+ i, getBrain(), condSet1);
				cond[i%nbA].setDestination(actions[(i+1)%nbA]);
				actions[i%nbA].addCondition(cond[i%nbA]);
			}
		}
		
		setFirstAction(this.actions[0]);
	}

	private WarAction<WarBaseAdapter> getActionInstance(EnumAction warAction) {
		try {
			return (WarAction<WarBaseAdapter>) Class.forName(Settings.getFullNameOf(warAction)).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
