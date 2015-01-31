package edu.warbot.FSMEditor.Modeles;

import java.util.ArrayList;

import edu.warbot.FSMEditor.Configuration;

public class ModeleState {
	
	private String name;
	private String planName;
	
	private ArrayList<ModeleCondition> conditionsOut = new ArrayList<>();
	private ArrayList<ModeleCondition> conditionsIn = new ArrayList<>();

	public ModeleState(String name, String planName) {
		this.name = name;
		this.planName = planName;
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

	public String getNom() {
		return this.name;
	}
	
	
	
	

}
