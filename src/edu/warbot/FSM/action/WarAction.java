package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSMEditor.settings.EnumAction;
import edu.warbot.brains.ControllableWarAgentAdapter;

public abstract class WarAction<AgentAdapterType extends ControllableWarAgentAdapter> {
	
	ArrayList<WarCondition<AgentAdapterType>> conditions = new ArrayList<>();
	
	private String nom;

	private AgentAdapterType agent;
	
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

	
	public String getName(){
		return this.nom;
	}

	public ArrayList<WarCondition<AgentAdapterType>> getConditions() {
		return this.conditions;
	}
	
	public void addCondition(WarCondition<AgentAdapterType> cond){
		this.conditions.add(cond);
	}
	
	/**
	 * Méthode appelée avant chaque première exécution de l'action
	 */
	public void actionWillBegin(){
		for (WarCondition<AgentAdapterType> warCondition : conditions) {
			warCondition.conditionWillBegin();
		}
		getAgent().setDebugString(this.getClass().getSimpleName());
	}

	public EnumAction getType() {
		return EnumAction.valueOf(getClass().getSimpleName());
	}

}
