package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.brains.ControllableWarAgentAdapter;

public abstract class WarAction<AgentAdapterType extends ControllableWarAgentAdapter> {
	
	ArrayList<WarCondition<AgentAdapterType>> conditions = new ArrayList<>();
	
	private AgentAdapterType agent;
	
	private String nom;
	
	private boolean terminate = false;
	
	public WarAction(AgentAdapterType b){
		this.agent = b;
		this.nom = this.getClass().getSimpleName();
	}
	
	public WarAction(AgentAdapterType b, String nom){
		this.agent = b;
		this.nom = nom;
	}
	
	/**
	 * Méthode appelée à chaque tik où l'action doit être executée
	 */
	public abstract String executeAction();

	public AgentAdapterType getAgent() {
		return this.agent;
	}

	
	public String getNom(){
		return this.nom;
	}

	public ArrayList<WarCondition<AgentAdapterType>> getConditions() {
		return this.conditions;
	}
	
	public void setActionTerminate(boolean b){
		this.terminate = b;
	}
	
	public boolean isTerminate(){
		return this.terminate;
	}
	
	public void addCondition(WarCondition<AgentAdapterType> cond){
		this.conditions.add(cond);
	}
	
	/**
	 * Méthode appelée avant chaque première exécution de l'action
	 */
	public void actionWillBegin(){
		setActionTerminate(false);
		getAgent().setDebugString(this.getClass().getSimpleName());
	}

}
