package edu.warbot.FSMEditor.models;

import java.util.ArrayList;
import java.util.Vector;

import edu.warbot.agents.enums.WarAgentType;

public class ModeleBrain {
	
	private WarAgentType agentType;
	
	private ArrayList<ModelState> sates = new ArrayList<>();
	private ArrayList<ModelCondition> conditions = new ArrayList<>();
	
	public ModeleBrain(WarAgentType agentType) {
		this.agentType = agentType;
	}

	public void addState(ModelState d) {
		this.sates.add(d);
	}
	
	public ArrayList<ModelState> getStates(){
		return this.sates;
	}

	public void addCondition(ModelCondition c) {
		this.conditions.add(c);
	}

	public void removeState(ModelState m) {
		//On supprime toutes les conditions de l'�tat source
		this.conditions.removeAll(m.getConditionsOut());
		
		//on parcours toutes les condtions pour voir si il yen a qui vont jusqua l'�tat qui va etre suppr
		ArrayList<ModelCondition> toDelet = new ArrayList<>();
		for (ModelCondition cond: this.conditions) {
			if(cond.getStateDestination().equals(m)){
				toDelet.add(cond);
			}
		}
		
		this.conditions.removeAll(toDelet);
		
		this.sates.remove(m);
	}

	public ArrayList<ModelCondition> getConditions() {
		return this.conditions;
	}

	public Vector<String> getConditionsName() {
		Vector<String> res = new Vector<>();
		for (ModelCondition m : this.conditions) {
			res.add(m.getName());
		}
		return res;
	}

	public String getAgentTypeName() {
		return this.agentType.toString();
	}
	
	public WarAgentType getAgentType() {
		return this.agentType;
	}

}
