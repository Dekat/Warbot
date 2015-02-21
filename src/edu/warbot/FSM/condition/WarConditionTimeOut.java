package edu.warbot.FSM.condition;

import edu.warbot.FSMEditor.settings.GenericConditionSettings;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionTimeOut<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType>{
	
	Integer timeOut;

	Integer currentTime = 0;
	
	public WarConditionTimeOut(String name, AgentAdapterType brain, 
			GenericConditionSettings conditionSettings){
		super(name, brain, conditionSettings);
		
		if(conditionSettings.Tik_number != null)
			this.timeOut = conditionSettings.Tik_number;
		else
			this.timeOut = 1;
	}

	@Override
	public void conditionWillBegin() {
		this.currentTime = 0;
	}
	
	@Override
	public boolean isValide() {
		currentTime++;
		if(currentTime > timeOut)
			return true;
		else
			return false;
			
	}

}
