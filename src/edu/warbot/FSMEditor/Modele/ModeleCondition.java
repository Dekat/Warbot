package edu.warbot.FSMEditor.Modele;

import edu.warbot.FSMEditor.dialogues.DialogueCondSetting;

public class ModeleCondition {
	
	String name;
	ModeleState modeleDest;
	ModeleState modeleSource;
	String type;
	
	//Attribut propre au type "attributCheck"
	String attCName;
	String attCOperateur;
	String attCValue;
	boolean attCPourcentage;
	
	public ModeleCondition(DialogueCondSetting d) {
		this.name = d.getNom();
		this.type = d.getConditionType();
		
		//Attribut propre au type "attributCheck"
		this.attCName = d.getAttributCheckName();
		this.attCOperateur = d.getAttributCheckOperateur();
		this.attCValue = d.getAttributCheckValue();
		this.attCPourcentage = d.getAttributCheckPourcentage();
	}

	public String getNom() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	public void setDestination(ModeleState d) {
		this.modeleDest = d;
	}
	
	public void setSource(ModeleState s) {
		this.modeleSource = s;
	}
	
	public ModeleState getDestination(){
		return this.modeleDest;
	}
	
	public ModeleState getSource(){
		return this.modeleSource;
	}

	public String getAttributCheckName() {
		return attCName;
	}
	
	public String getAttributCheckOperateur() {
		return attCOperateur;
	}
	
	public String getAttributCheckValue() {
		return attCValue;
	}
	
	public boolean getAttributCheckPourcentage() {
		return attCPourcentage;
	}

}
