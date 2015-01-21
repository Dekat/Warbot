package edu.warbot.FSMEditor.Modele;

import edu.warbot.FSMEditor.dialogues.DialogueCondSetting;

public class ModeleCondition {
	
	String name;
	ModeleState modeleDest;
	String type;
	
	public ModeleCondition(DialogueCondSetting d) {
		this.name = d.getNom();
		this.type = d.getConditionType();
	}

	public String getNom() {
		return name;
	}

	public void setDestination(ModeleState d) {
		this.modeleDest = d;
	}
	
	public ModeleState getDestination(){
		return this.modeleDest;
	}

}
