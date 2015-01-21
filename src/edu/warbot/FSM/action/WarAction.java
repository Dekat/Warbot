package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.brains.WarBrain;

public abstract class WarAction {
	
	ArrayList<WarCondition> conditions = new ArrayList<>();
	
	private WarBrain brain;
	
	private String nom;
	
	private boolean terminate = false;
	
	public WarAction(WarBrain b){
		this.brain = b;
		this.nom = this.getClass().getSimpleName();
	}
	
	public WarAction(WarBrain b, String nom){
		this.brain = b;
		this.nom = nom;
	}
	
	/**
	 * M�thode appel� a chaque tik o� l'action doit etre execut�
	 */
	public abstract String executeAction();

	/**
	 * ATTENTION cette m�thode doit etre redefinit pour 
	 * avoir acces aux m�thodes des agents specfiques sur 
	 * lesquels l'action va s'appliquer
	 */
	public WarBrain getBrain(){
		return this.brain;
	}

	
	public String getNom(){
		return this.nom;
	}

	public ArrayList<WarCondition> getConditions() {
		return this.conditions;
	}
	
	public void setActionTerminate(boolean b){
		this.terminate = b;
	}
	
	public boolean isTerminate(){
		return this.terminate;
	}
	
	public void addCondition(WarCondition cond){
		this.conditions.add(cond);
	}
	
	/**
	 * M�thode appel� avant chaque premi�re execution de l'action
	 */
	public void actionWillBegin(){
		setActionTerminate(false);
		getBrain().setDebugString(this.getClass().getSimpleName());
	}

}
