package edu.warbot.FSM.condition;

import edu.warbot.FSMEditor.settings.GenericConditionSettings;
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
			GenericConditionSettings conditionSettings){

		super(name, brain, conditionSettings);
		
		this.agentType = conditionSettings.Agent_type;
		
		if(conditionSettings.Operateur != null)
			this.operand = conditionSettings.Operateur;
		else
			this.operand = EnumOperand.egal;
					
		if(conditionSettings.Reference != null)
			this.reference = conditionSettings.Reference;
		else
			this.reference = 1;
		
		if(conditionSettings.Enemie != null)
			this.enemy = conditionSettings.Enemie;
		else
			enemy = true;
		
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
		case egal:
			return nbPercept == this.reference;
		case dif:
			return nbPercept != this.reference;
		default:
			System.err.println("FSM : unknown operateur " + this.operand + this.getClass());
			return false;
		}
		
			
	}

}
