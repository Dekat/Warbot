package edu.warbot.FSMEditor.models;

import java.util.ArrayList;

import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSMEditor.settings.PlanEnum;
import edu.warbot.FSMEditor.settings.Settings;

public class ModeleState {
	
	private String name;
	private PlanEnum warPlanName;
	
	private WarPlanSettings planSettings;
	
	//TODO ecq il faut laisser ça ici ?
	private ArrayList<String> conditionsOutID = new ArrayList<String>();
	
	private ArrayList<ModeleCondition> conditionsOut = new ArrayList<>();
	private ArrayList<ModeleCondition> conditionsIn = new ArrayList<>();

	public ModeleState(String name, PlanEnum planName, WarPlanSettings planSettings) {
		this.name = name;
		this.warPlanName = planName;
		this.planSettings = planSettings;
	}

	public void addConditionOut(ModeleCondition mc) {
		this.conditionsOut.add(mc);
//		this.conditionsOutID.add(mc.getName());
	}

	public void addConditionIn(ModeleCondition condition) {
		this.conditionsIn.add(condition);
	}
	
	/*** Accesseurs ***/
	public ArrayList<ModeleCondition> getConditionsOut(){
		return this.conditionsOut;
	}
	
	public ArrayList<ModeleCondition> getConditionsIn(){
		return this.conditionsIn;
	}

	public String getPlanLoaderName(){
		return Settings.getFullNameOf(this.warPlanName);
	}
	
	public PlanEnum getPlanName(){
		return warPlanName;
	}
	
	public String getName() {
		return this.name;
	}

	public void setConditionsOutID(ArrayList<String> condID) {
		this.conditionsOutID = condID;		
	}
	
	public ArrayList<String> getConditionsOutID() {
		return this.conditionsOutID;		
	}
	
	public WarPlanSettings getPlanSettings(){
		return this.planSettings;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlanName(PlanEnum planName) {
		this.warPlanName = planName;
	}

	public void addConditionOutID(String name) {
		this.conditionsOutID.add(name);
	}

	public void setPlanSettings(WarPlanSettings planSettings) {
		this.planSettings = planSettings;
	}
	
	

}
