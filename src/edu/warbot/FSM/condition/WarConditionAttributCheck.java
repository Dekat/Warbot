package edu.warbot.FSM.condition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionAttributCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType> {
	
	Integer attribut;
	Integer reference;
	String operand;
	
	String nameAtt;
	Method methode;
	
	public WarConditionAttributCheck(String name, AgentAdapterType brain, WarConditionSettings conditionSettings){
		
		super(name, brain, conditionSettings);
		this.nameAtt = conditionSettings.Attribut_name;
		this.operand = conditionSettings.Operateur;
		this.reference = conditionSettings.Reference;
		
		
		try {
			this.methode = this.brain.getClass().getMethod(this.nameAtt);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isValide() {
				
		try {
			this.attribut = Integer.valueOf((String) methode.invoke(this.brain));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//TODO faire des verifations de type
		
		switch (this.operand) {
		case "<":
			return (Integer)this.attribut < (Integer)this.reference;
		case ">":
			return (Integer)this.attribut > (Integer)this.reference;
		case "==":
			return (Integer)this.attribut == (Integer)this.reference;
		case "<=":
			return (Integer)this.attribut <= (Integer)this.reference;
		case ">=":
			return (Integer)this.attribut >= (Integer)this.reference;
		default:
			System.err.println("FSM : unknown operateur " + this.operand);
			return false;
		}
			
	}
	
	public static final String HEALTH = "getHealth";
	public static final String NB_ELEMEN_IN_BAG = "getNbElementsInBag";

}
