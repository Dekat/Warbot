package edu.warbot.FSM.condition;

import java.util.ArrayList;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarConditionPerceptAttributCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType> {
	
	int attribut;
	int reference;
	String operand;
	
	String nameAtt;
	
	boolean enemy;
	WarAgentType agentType;
	boolean oneOf;
	
	/**
	 * 
	 * @param brain
	 * @param nameAtt
	 * @param operand
	 * @param ref
	 * @param poucentage
	 * @param enemy = true, ally = false
	 * @param agentType null for all
	 * @param oneOf oneOf = true, all = false
	 */
	public WarConditionPerceptAttributCheck(AgentAdapterType brain, String nameAtt,
			String operand, int ref, int poucentage, boolean enemy, WarAgentType agentType, boolean oneOf) {
		
		this(brain, nameAtt, operand, (Integer)(ref*poucentage/100), enemy, agentType, oneOf);

	}
	
	public WarConditionPerceptAttributCheck(AgentAdapterType brain, String nameAtt,
			String operand, int ref, boolean enemy, WarAgentType agentType, boolean oneOf) {
		
		super(brain);
		this.nameAtt = nameAtt;
		this.operand = operand;
		this.reference = ref;
		
		this.enemy = enemy;
		this.agentType = agentType;
		this.oneOf = oneOf;
		
	}

	@Override
	public boolean isValide() {

		// Recupere les percepts
		ArrayList<WarPercept> percepts = new ArrayList<>();
		
		if(this.enemy){
			if(this.agentType == null){
				percepts= getBrain().getPerceptsEnemies();
			}else{
				percepts = getBrain().getPerceptsEnemiesByType(this.agentType);
			}
		}else{
			if(this.agentType == null){
				percepts= getBrain().getPerceptsAllies();
			}else{
				percepts = getBrain().getPerceptsAlliesByType(this.agentType);
			}
		}
		
		if(percepts == null || percepts.size() == 0)
			return false;
		
		// Recupere des percepts, l'attributs qui va etre traité
		ArrayList<Integer> listeAttriuts = new ArrayList<>();
		if(this.oneOf){
			listeAttriuts.add(getAttribut(percepts.get(0), this.nameAtt));
		}else{
			for (WarPercept p : percepts) {
				listeAttriuts.add(getAttribut(p, this.nameAtt));
			}
		}
		
		// Fait les verifications
		boolean allAttValid = true;
		switch (this.operand) {
		case "<":
			for (Integer att : listeAttriuts) {
				if(!(att < this.reference))
					allAttValid = false;
			}
			return allAttValid;
		case ">":
			for (Integer att : listeAttriuts) {
				if(!(att > this.reference))
					allAttValid = false;
			}
			return allAttValid;
		case "==":
			for (Integer att : listeAttriuts) {
				if(!(att == this.reference))
					allAttValid = false;
			}
			return allAttValid;
		default:
			System.err.println("FSM : unknown operateur " + this.operand + this.getClass());
			return false;
		}
		
			
	}
	
	private Integer getAttribut(WarPercept p, String att) {
		if(att.equals(WarConditionPerceptAttributCheck.HEALTH))
			return p.getHealth();
		else{
			System.out.println("Attribut not known " + this.getClass());
			return -1;
		}
	}

	public static final String HEALTH = "getHealth";
	public static final String NB_ELEMEN_IN_BAG = "getNbElementsInBag";
}
