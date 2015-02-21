package edu.warbot.FSMEditor.models;

import edu.warbot.FSMEditor.views.ViewBrain;
import edu.warbot.agents.enums.WarAgentType;

import java.util.ArrayList;

public class ModeleBrain {
	
	private WarAgentType agentType;
	
	private ModelState firstState;
	
	private String firstStateID;

	private ArrayList<ModelState> sates = new ArrayList<>();
	private ArrayList<ModelCondition> conditions = new ArrayList<>();
	
	public ModeleBrain(WarAgentType agentType) {
		this.agentType = agentType;
	}

	public void addState(ModelState d) {
		this.sates.add(d);
		if(this.firstState == null)
			this.firstState = d;
	}
	
	public void addCondition(ModelCondition c) {
		this.conditions.add(c);
	}

	public ArrayList<ModelState> getStates(){
		return this.sates;
	}

	public void removeState(ModelState m) {
		for (ModelCondition modelCondition : m.getConditionsOut()) {
			modelCondition.setSource(null);
		}

		for (ModelCondition modelCondition : conditions) {
			if(modelCondition.getStateDestination().equals(m)){
				modelCondition.setDestination(null);
				modelCondition.setStateOutId(null);
			}
			
		}
		
		this.sates.remove(m);
	}

	public void removeCondition(ModelCondition modeleCond) {
		//Supprime la condition en tant que condition de sortie des états
		for (ModelState modelS : sates) {
			if(modelS.getConditionsOut().contains(modeleCond))
				modelS.removeConditionOut(modeleCond);
		}

		//Supprime la condition
		conditions.remove(modeleCond);
		
	}

	public ArrayList<ModelCondition> getConditions() {
		return this.conditions;
	}

	public String getAgentTypeName() {
		return this.agentType.toString();
	}
	
	public WarAgentType getAgentType() {
		return this.agentType;
	}

	/**** Les modeles brains connaissent leurs vue ***/
	ViewBrain viewBrain;


	public void setViewBrain(ViewBrain vb) {
		this.viewBrain = vb;
	}
	
	public ViewBrain getViewBrain(){
		return viewBrain;
	}

	public void setFirstState(ModelState modelState) {
		this.firstState = modelState;
	}

	public ModelState getFirstState() {
		return firstState;
	}

	public void setFirstStateID(String first) {
		this.firstStateID = first;
	}
	
	public String getFirstStateID() {
		return this.firstStateID;
	}

}
