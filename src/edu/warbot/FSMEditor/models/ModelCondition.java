package edu.warbot.FSMEditor.models;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSMEditor.settings.ConditionEnum;
import edu.warbot.FSMEditor.settings.Settings;

public class ModelCondition {
	
	private String name;
	private ConditionEnum typeName;
	private WarConditionSettings conditionGenericAttributs;

	ModelState modeleDest;
	ModelState modeleSource;
	
	String stateOutId;
	
	ConditionEnum conditionType;
	
	public ModelCondition(String name, ConditionEnum type, WarConditionSettings conditionSettings){
		this.name = name;
		this.typeName = type;
		this.conditionGenericAttributs = conditionSettings;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ConditionEnum getType() {
		return typeName;
	}
	
	public String getTypeLoaderName() {
		return Settings.getFullNameOf(typeName);
	}
	
	public void setDestination(ModelState d) {
		this.modeleDest = d;
//		this.stateOutId = d.getName();
	}
	
	public void setSource(ModelState modelState) {
		this.modeleSource = modelState;
		modelState.addConditionOut(this);
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

	public WarConditionSettings getConditionSettings() {
		return this.conditionGenericAttributs;
	}

	public void setConditionType(ConditionEnum conditionType) {
		this.conditionType = conditionType;
	}
	
	public ConditionEnum getConditionType() {
		return this.conditionType;
	}

}
