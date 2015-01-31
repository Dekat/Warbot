package edu.warbot.FSM.reflexe;

import edu.warbot.brains.ControllableWarAgentAdapter;

public abstract class WarReflexe<AgentAdapterType extends ControllableWarAgentAdapter> {
	
	private AgentAdapterType brain;

	private String nom;
	
	public WarReflexe(AgentAdapterType b){
		this.brain = b;
		this.nom = this.getClass().getSimpleName();
	}
	
	public WarReflexe(AgentAdapterType b, String nom){
		this.brain = b;
		this.nom = nom;
	}

	public abstract String executeReflexe();

	public AgentAdapterType getBrain(){
		return this.brain;
	}
	
	public String getNom(){
		return this.nom;
	}

}
