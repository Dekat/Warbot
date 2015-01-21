package edu.warbot.FSM.Reflexe;

import edu.warbot.brains.WarBrain;

public abstract class WarReflexe {
	
	private WarBrain brain;

	private String nom;
	
	public WarReflexe(WarBrain b){
		this.brain = b;
		this.nom = this.getClass().getSimpleName();
	}
	
	public WarReflexe(WarBrain b, String nom){
		this.brain = b;
		this.nom = nom;
	}

	public abstract String executeReflexe();

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

}
