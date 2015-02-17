package edu.warbot.FSM;

import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.plan.WarPlan;
import edu.warbot.brains.ControllableWarAgentAdapter;

import java.util.ArrayList;

public class WarEtat<AgentAdapterType extends ControllableWarAgentAdapter> {
	
	private WarPlan<? extends AgentAdapterType> plan;
	private ArrayList<WarCondition<AgentAdapterType>> conditions = new ArrayList<>();
	
	private String nom;
	
	public WarEtat(String nom, WarPlan<? extends AgentAdapterType> plan){
		this.nom = nom;
		this.plan = plan;
	}

	public void addCondition(WarCondition<AgentAdapterType> cond1) {
		this.conditions.add(cond1);
	}

	public WarPlan<? extends AgentAdapterType> getPlan() {
		return this.plan;
	}
	
	public ArrayList<WarCondition<AgentAdapterType>> getConditions() {
		return this.conditions;
	}

	public void initEtat() {
		
		if(this.plan == null){
			System.err.println("WarEtat : ERREUR : un état doit obligatoirement contenir un plan <" + this.nom + ">");
		}
		
		if(this.conditions.size() < 1)
			System.out.println("ATTENTION l'état <" + this.nom + "> ne contient aucune conditions de sortie");
		
		for (WarCondition<AgentAdapterType> cond : this.conditions) {
			cond.init();
		}
		
		System.out.println("\tL'état <" + this.getName() + "> " + "contient le plan <" + this.getPlan().getNom() + ">" + " et " + this.conditions.size() + " conditions de sorties");
		
		this.plan.initPlan();
		
	}

	public String getName() {
		return this.nom;
	}

	public void stateWillBegin() {
		//On dit l'état qu'il va commencer
		this.plan.planWillBegin();
		//On dit aux conditions qu'elles vont commencer
		for (WarCondition<AgentAdapterType> warCondition : conditions) {
			warCondition.conditionWillBegin();
		}
	}

}
