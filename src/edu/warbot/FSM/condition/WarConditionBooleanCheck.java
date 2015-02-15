package edu.warbot.FSM.condition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionBooleanCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType> {
	
	EnumMethod methodName;
	Method method;
	
	public WarConditionBooleanCheck(String name, AgentAdapterType brain, WarConditionSettings conditionSettings){
		
		super(name, brain, conditionSettings);
		methodName = conditionSettings.Methode;
		
		try {
			this.method = this.brain.getClass().getMethod(this.methodName.name());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			System.err.println("no suck method name for " + this.methodName);
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isValide() {
		Boolean currentValue = null;
		try {
			currentValue = (Boolean)(method.invoke(this.brain));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return currentValue;
			
	}
	
}
