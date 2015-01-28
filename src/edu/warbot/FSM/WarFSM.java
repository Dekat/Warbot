package edu.warbot.FSM;

import java.util.ArrayList;

import edu.warbot.FSM.Reflexe.WarReflexe;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.brains.ControllableWarAgentAdapter;

public class WarFSM<AgentAdapterType extends ControllableWarAgentAdapter> {
	
	
	private WarEtat<AgentAdapterType> firstEtat;
	private WarEtat<AgentAdapterType> etatCourant;

	private ArrayList<WarEtat<AgentAdapterType>> listeEtat = new ArrayList<>();
	private ArrayList<WarReflexe<AgentAdapterType>> listeReflexe = new ArrayList<>();
	

	public void initFSM() {
		System.out.println("*** Initialisation de la FSM ***");
		System.out.println("La FSM contient " + this.listeEtat.size() + " états");
		
		
		if(this.listeEtat.size() < 1){
			System.err.println("ERREUR La FSM doit contenir au moins 1 état !");
			System.exit(0);
		}
		
		if(firstEtat == null){
			this.firstEtat = listeEtat.get(0);
			System.out.println("ATTENTION vous devez choisir un état de depart : par defaut le premier état ajouter est choisit comme état de départ <" + this.firstEtat.getNom() + ">");
		}
	
		this.etatCourant = firstEtat;
		
		for (WarEtat<AgentAdapterType> e : this.listeEtat) {
			e.initEtat();
		}
	
		
	}
	
	public String executeFSM(){
		String actionResultat = null;
		
		if(this.listeReflexe == null | this.listeEtat == null | this.etatCourant == null){
			System.err.println("La FSM a des pointeur null etes vous sur de l'avoir ben initialisé ?");
			System.exit(0);
		}
		
		//On execute les reflexes
		for (WarReflexe<AgentAdapterType> r : this.listeReflexe) {
			actionResultat = r.executeReflexe();
		}
		
		//On execute le plan courant
		if(actionResultat == null)
			actionResultat = this.etatCourant.getPlan().executePlan();

		//On change d'état si besoin
		ArrayList<WarCondition<AgentAdapterType>> conditions = this.etatCourant.getConditions();
			
		for (WarCondition<AgentAdapterType> conditionCourante : conditions) {
			if(conditionCourante.isValide()){
				this.etatCourant = conditionCourante.getEtatDestination();
				break;
			}
		}
		
		return actionResultat;
		
	}

	public void addEtat(WarEtat<AgentAdapterType> e) {
		if(this.listeEtat.contains(e))
			System.out.println("ATTENTION le plan <" + e.getNom() + "> a deja ete ajoute a la FSM");
		this.listeEtat.add(e);
		
	}
	
	public void setFirstEtat(WarEtat<AgentAdapterType> e){
		this.firstEtat = e;
	}

	public void addReflexe(WarReflexe<AgentAdapterType> r) {
		this.listeReflexe.add(r);
	}

}
