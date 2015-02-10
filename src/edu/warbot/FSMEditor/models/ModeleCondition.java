package edu.warbot.FSMEditor.models;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSMEditor.FSMSettings.ConditionEnum;
import edu.warbot.FSMEditor.FSMSettings.Settings;

public class ModeleCondition {
	
	private String name;
	private ConditionEnum typeName;
	private WarConditionSettings conditionGenericAttributs;

	ModeleState modeleDest;
	ModeleState modeleSource;
	
	String stateOutId;
	
	ConditionEnum conditionType;
	
	public ModeleCondition(String name, ConditionEnum type, WarConditionSettings conditionSettings){
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
	
	public void setDestination(ModeleState d) {
		this.modeleDest = d;
//		this.stateOutId = d.getName();
	}
	
	public void setSource(ModeleState modelState) {
		this.modeleSource = modelState;
		modelState.addConditionOut(this);
	}
	
	public ModeleState getStateDestination(){
		return this.modeleDest;
	}
	
	public ModeleState getStateSource(){
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
