package edu.warbot.FSM.condition;

import edu.warbot.FSM.WarGenericSettings.ConditionSettings;
import edu.warbot.FSMEditor.settings.EnumOperand;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.ControllableWarAgentAdapter;

import java.util.ArrayList;

public class WarConditionPerceptCounter<AgentAdapterType extends ControllableWarAgentAdapter> extends WarCondition<AgentAdapterType> {
	
	int reference;
	EnumOperand operand;
	
	boolean enemy;
	WarAgentType agentType;
	
	public WarConditionPerceptCounter(String name, AgentAdapterType brain, 
			ConditionSettings conditionSettings){

		super(name, brain, conditionSettings);
		
		this.agentType = conditionSettings.Agent_type;
		this.operand = conditionSettings.Operateur;
		this.reference = conditionSettings.Reference;
		
		this.enemy = conditionSettings.Enemie;
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
		
		// Fait les verifications
		int nbPercept = percepts.size();

		switch (this.operand) {
		case inf:
			return nbPercept < this.reference;
		case sup:
			return nbPercept > this.reference;
		case eg:
			return nbPercept == this.reference;
		default:
			System.err.println("FSM : unknown operateur " + this.operand + this.getClass());
			return false;
		}
		
			
	}

}
