package edu.warbot.FSM.condition;

import edu.warbot.FSM.WarGenericSettings.ConditionSettings;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionTimeOut<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType>{
	
	Integer timeOut;

	Integer currentTime = 0;
	
	public WarConditionTimeOut(String name, AgentAdapterType brain, 
			ConditionSettings conditionSettings){
		super(name, brain, conditionSettings);
		
		this.timeOut = conditionSettings.Tik_number;
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
