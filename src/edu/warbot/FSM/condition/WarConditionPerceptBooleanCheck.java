package edu.warbot.FSM.condition;

import edu.warbot.FSM.genericSettings.ConditionSettings;
import edu.warbot.FSMEditor.settings.EnumOperand;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.ControllableWarAgentAdapter;

import java.util.ArrayList;

public class WarConditionPerceptBooleanCheck<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType> {
	
	int attribut;
	int reference;
	EnumOperand operand;
	
	String nameAtt;
	
	boolean enemy;
	WarAgentType agentType;
	boolean oneOf;
	
	public WarConditionPerceptBooleanCheck(String name, AgentAdapterType brain, 
			ConditionSettings conditionSettings){
		
		super(name, brain, conditionSettings);
//		this.nameAtt = conditionSettings.Reference;
		this.operand = conditionSettings.Operateur;
		this.reference = conditionSettings.Reference;
		
		this.enemy = conditionSettings.Enemie;
		this.agentType = conditionSettings.Agent_type;
		this.oneOf = conditionSettings.Offensif;
		
	}

	@Override
	public boolean isValide() {

		// Recupere les percepts
		ArrayList<WarAgentPercept> percepts = new ArrayList<>();
		
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
			for (WarAgentPercept p : percepts) {
				listeAttriuts.add(getAttribut(p, this.nameAtt));
			}
		}
		
		// Fait les verifications
		boolean allAttValid = true;
		switch (this.operand) {
		case inf:
			for (Integer att : listeAttriuts) {
				if(!(att < this.reference))
					allAttValid = false;
			}
			return allAttValid;
		case sup:
			for (Integer att : listeAttriuts) {
				if(!(att > this.reference))
					allAttValid = false;
			}
			return allAttValid;
		case egal:
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
	
	private Integer getAttribut(WarAgentPercept p, String att) {
		if(att.equals(WarConditionPerceptBooleanCheck.HEALTH))
			return p.getHealth();
		else{
			System.out.println("Attribut not known " + this.getClass());
			return -1;
		}
	}

	public static final String HEALTH = "getHealth";
	public static final String NB_ELEMEN_IN_BAG = "getNbElementsInBag";
}