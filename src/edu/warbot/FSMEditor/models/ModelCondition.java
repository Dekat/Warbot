package edu.warbot.FSMEditor.models;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSMEditor.settings.EnumCondition;
import edu.warbot.FSMEditor.settings.Settings;

public class ModelCondition {
	
	private String name;
	private EnumCondition typeName;
	private WarConditionSettings conditionGenericAttributs;

	ModelState modeleSource;
	ModelState modeleDest;
	
	String stateOutId;
	
	EnumCondition conditionType;
	
	public ModelCondition(String name, EnumCondition type, WarConditionSettings conditionSettings){
		this.name = name;
		this.typeName = type;
		this.conditionGenericAttributs = conditionSettings;
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
		return typeName;
	}
	
	public String getTypeLoaderName() {
		return Settings.getFullNameOf(typeName);
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

	public void setConditionType(EnumCondition conditionType) {
		this.conditionType = conditionType;
	}
	
	public EnumCondition getConditionType() {
		return this.conditionType;
	}

}
