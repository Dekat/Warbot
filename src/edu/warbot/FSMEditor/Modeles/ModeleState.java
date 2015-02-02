package edu.warbot.FSMEditor.Modeles;

import java.util.ArrayList;

import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.Configuration;

public class ModeleState {
	
	private String name;
	private String planName;
	
	private WarPlanSettings planSettings;
	
	//TODO ecq il faut laisser �a ici ?
	private ArrayList<String> conditionsOutID = new ArrayList<String>();
	
	private ArrayList<ModeleCondition> conditionsOut = new ArrayList<>();
	private ArrayList<ModeleCondition> conditionsIn = new ArrayList<>();

	public ModeleState(String name, String planName, WarPlanSettings planSettings) {
		this.name = name;
		this.planName = planName;
		this.planSettings = planSettings;
	}

	public void addConditionOut(ModeleCondition mc) {
		this.conditionsOut.add(mc);
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

	public String getPlanName(){
		return this.planName;
	}
	
	public String getSimplePlanName(){
		return Configuration.getSimpleName(this.planName);
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
	
	public WarPlanSettings getWarPlanSettings(){
		return this.planSettings;
	}
	
	

}
