package edu.warbot.FSM.condition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionAttributCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType> {
	
	Object attribut;
	Object reference;
	String operand;
	
	String nameAtt;
	Method methode;
	
	public WarConditionAttributCheck(AgentAdapterType brain, String attributName,
			String operand, Object reference, int poucentage) {
		
		this(brain, attributName, operand, (Integer)((Integer)(reference)*poucentage/100));
		
		//double a = (Integer)((Integer)(ref)*poucentage/100);
		//System.out.println(a);
	}
	
	public WarConditionAttributCheck(AgentAdapterType brain, String nameAtt,
			String operand, Object ref) {
		
		super(brain);
		this.nameAtt = nameAtt;
		this.operand = operand;
		this.reference = ref;
		
		
		try {
			this.methode = this.brain.getClass().getMethod(this.nameAtt);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isValide() {
				
		try {
			this.attribut = methode.invoke(this.brain);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
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
