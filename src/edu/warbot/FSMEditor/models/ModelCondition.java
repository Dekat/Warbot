package edu.warbot.FSMEditor.models;

import edu.warbot.FSM.genericSettings.ConditionSettings;
import edu.warbot.FSMEditor.settings.EnumCondition;
import edu.warbot.FSMEditor.settings.Settings;

public class ModelCondition {
	
	private String name;
	private ConditionSettings conditionSettings;

	ModelState modeleSource;
	ModelState modeleDest;
	
	String stateOutId;
	
	EnumCondition conditionType;
	
	public ModelCondition(String name, EnumCondition type, ConditionSettings conditionSettings){
		this.name = name;
		this.conditionType = type;
		this.conditionSettings = conditionSettings;
	}
	
	public void setSource(ModelState modelState) {
		this.modeleSource = modelState;
		
//		if(modelState.getConditionsOut() == null || !modelState.getConditionsOut().contains(this))
//			modelState.addConditionOut(this);
	}

	public void setDestination(ModelState d) {
		this.modeleDest = d;
//		d.addConditionIn(this);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public EnumCondition getType() {
		return conditionType;
	}
	
	public ModelState getStateDestination(){
		return this.modeleDest;
	}
	
	public ModelState getStateSource(){
		return this.modeleSource;
	}

	public String getStateOutId() {
		return stateOutId;
	}

	public void setStateOutId(String stateOutId) {
		this.stateOutId = stateOutId;
	}

	public ConditionSettings getConditionSettings() {
		return this.conditionSettings;
	}

	public void setConditionType(EnumCondition conditionType) {
		this.conditionType = conditionType;
	}
	
	public EnumCondition getConditionType() {
		return this.conditionType;
	}

	public String getConditionLoaderName() {
		return Settings.getFullNameOf(conditionType);
	}

	public void setConditionSettings(ConditionSettings conditionSettings) {
		this.conditionSettings = conditionSettings;
	}

}
