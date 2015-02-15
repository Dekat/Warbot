package edu.warbot.FSM.condition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.FSMEditor.settings.EnumOperand;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionAttributCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType> {
	
	//Je veux que cette condition permet de faire par exemple : ma vie > 1200  et mon sac > 4 (mon sac isFull)
	
	EnumMethod methodName;
	EnumOperand operand;
	Integer reference;
	
//	String nameAtt;
	Method method;
	
	public WarConditionAttributCheck(String name, AgentAdapterType brain, WarConditionSettings conditionSettings){
		
		super(name, brain, conditionSettings);
		this.reference = conditionSettings.Reference;
		this.operand = conditionSettings.Operateur;
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
		Integer currentValue = null;
		try {
			currentValue = (Integer)(method.invoke(this.brain));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		switch (this.operand) {
		case inf:
			return (Integer)currentValue < (Integer)this.reference;
		case sup:
			return (Integer)currentValue > (Integer)this.reference;
		case eg:
			return currentValue == this.reference;
		case infEg:
			return (Integer)currentValue <= (Integer)this.reference;
		case supEg:
			return (Integer)currentValue >= (Integer)this.reference;
		default:
			System.err.println("FSM : unknown operateur " + this.operand);
			return false;
		}
			
	}
	
}
