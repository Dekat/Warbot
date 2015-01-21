package edu.warbot.FSMEditor.Modele;

import java.util.ArrayList;

import edu.warbot.FSMEditor.dialogues.DialogueStateSetting;

public class ModeleState {
	
	private String name;
	private String planName;
	
	private ArrayList<ModeleCondition> conditions = new ArrayList<>();

	public ModeleState(DialogueStateSetting d) {
		this.name = d.getNom();
		this.planName = d.getPlanName();
	}

	public String getPlanName(){
		return this.planName;
	}

	public void addCondition(ModeleCondition mc) {
		this.conditions.add(mc);
	}

	public String getNom() {
		return this.name;
	}
	
	public ArrayList<ModeleCondition> getModeleConditions(){
		return this.conditions;
	}

}
