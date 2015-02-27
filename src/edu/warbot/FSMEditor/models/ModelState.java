package edu.warbot.FSMEditor.models;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import edu.warbot.FSMEditor.panels.PanelState;
import edu.warbot.FSMEditor.settings.EditorSettings;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;

public class ModelState {
	
	private String name;
	private String planName;
	
	private GenericPlanSettings planSettings;
	
	private ArrayList<String> conditionsOutID = new ArrayList<String>();
	
	private ArrayList<ModelCondition> conditionsOut = new ArrayList<>();
	private ArrayList<ModelCondition> conditionsIn = new ArrayList<>();

	public ModelState(String name, String planName, GenericPlanSettings planSettings) {
		this.name = name;
		this.planName = planName;
		this.planSettings = planSettings;
	}

	public void addConditionOut(ModelCondition mc) {
		this.conditionsOut.add(mc);
		
//		if(mc.getStateSource() == null /*| !mc.getStateSource().equals(this)*/)
		try{
			mc.setSource(this);
		}catch(NullPointerException e){
			JOptionPane.showMessageDialog(null, "Erro while creating fsm."
					+ "\nState with name <" + this.name + "> can't have source condition."
					+ "\nCheck your fsm."
					, "Loading error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void addConditionIn(ModelCondition condition) {
		this.conditionsIn.add(condition);
	}
	
	/*** Accesseurs ***/
	public ArrayList<ModelCondition> getConditionsOut(){
		return this.conditionsOut;
	}
	
	public ArrayList<ModelCondition> getConditionsIn(){
		return this.conditionsIn;
	}

	public String getPlanName(){
		return planName;
	}
	
	public String getPlanSimpleName(){
		return EditorSettings.getSimpleName(planName);
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
	
	public GenericPlanSettings getPlanSettings(){
		return this.planSettings;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public void addConditionOutID(String name) {
		this.conditionsOutID.add(name);
	}

	public void setPlanSettings(GenericPlanSettings planSettings) {
		this.planSettings = planSettings;
	}

	/*** Le modelState connait son viewState ***/
	private PanelState viewState;
	
	public void setViewState(PanelState ps) {
		this.viewState = ps;
	}
	
	public PanelState getViewState(){
		return viewState;
	}

	public void removeConditionOut(ModelCondition modCond) {
		this.conditionsOut.remove(modCond);
		this.conditionsOutID.remove(modCond.getName());
	}

}
