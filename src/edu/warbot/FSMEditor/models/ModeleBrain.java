package edu.warbot.FSMEditor.models;

import java.util.ArrayList;
import java.util.Vector;

import edu.warbot.FSMEditor.views.ViewBrain;
import edu.warbot.agents.enums.WarAgentType;

public class ModeleBrain {
	
	private WarAgentType agentType;
	
	private ModelState firstState;

	private ArrayList<ModelState> sates = new ArrayList<>();
	private ArrayList<ModelCondition> conditions = new ArrayList<>();
	
	public ModeleBrain(WarAgentType agentType) {
		this.agentType = agentType;
	}

	public void addState(ModelState d) {
		this.sates.add(d);
	}
	
	public void addCondition(ModelCondition c) {
		this.conditions.add(c);
	}

	public ArrayList<ModelState> getStates(){
		return this.sates;
	}

	public void removeState(ModelState m) {
		this.sates.remove(m);
	}

	public void removeCondition(ModelCondition modele) {
		conditions.remove(modele);
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
//		this.firstState.setFirstState(false);
		this.firstState = modelState;
//		this.firstState.setFirstState(true);
	}

	public ModelState getFirstState() {
		return firstState;
	}

}
