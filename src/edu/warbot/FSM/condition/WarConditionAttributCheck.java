package edu.warbot.FSM.condition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;

import edu.warbot.FSMEditor.settings.GenericConditionSettings;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.FSMEditor.settings.EnumOperand;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionAttributCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType> {
	
	
	EnumMethod methodName;
	EnumOperand operand;
	Integer reference;
	
	Method method;
	
	public WarConditionAttributCheck(String name, AgentAdapterType brain, GenericConditionSettings conditionSettings){
		super(name, brain, conditionSettings);

		JOptionPane.showMessageDialog(null, "Attention la condition <Attributcheck> n'a pas été vérifié et risque de ne pas fonctionner", "Waring not terminated condition", JOptionPane.WARNING_MESSAGE);
		
		this.reference = conditionSettings.Reference;
		this.operand = conditionSettings.Operateur;
		this.methodName = conditionSettings.Methode;
		
		
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
			return currentValue < this.reference;
		case sup:
			return currentValue > this.reference;
		case egal:
			return currentValue == this.reference;
		case dif:
			return currentValue != this.reference;
		default:
			System.err.println("FSM : unknown operateur " + this.operand);
			return false;
		}
			
	}
	
}
