package edu.warbot.FSM.plan;

import java.util.ArrayList;

import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.brains.ControllableWarAgentAdapter;

public abstract class WarPlan<AgentAdapterType extends ControllableWarAgentAdapter> {

	private WarAction<AgentAdapterType> actionCourante;
	private WarAction<AgentAdapterType> firstAction;
	private ArrayList<WarAction<AgentAdapterType>> actions = new ArrayList<>();
	
	private String nom;
	private AgentAdapterType brain;
	private WarPlanSettings planSettings;

	public WarPlan(String nomPlan, AgentAdapterType brain, WarPlanSettings planSettings){
		this.nom = nomPlan;
		this.brain = brain;
		this.planSettings = planSettings;
	}
	
	/**
	 * Ajoute les actions qui contituent le plan.
	 * Les actions doivent etre liées avec des conditions
	 */
	public abstract void buildActionList();
	
	public void initPlan() {
		this.buildActionList();
		
		if(this.actions.size() < 1){
			System.err.println("ERREUR le plan <" + this.nom + "> ne contient aucune actions à executer");
			System.exit(0);
		}
		
		if(this.firstAction == null){
			this.firstAction = actions.get(0);
			System.out.println("ATTENTION vous devez choisir une action de depart : par defaut la première action ajouter est choisit comme action de départ <" + this.firstAction.getName() + ">");
		}
	
		
		this.actionCourante = this.firstAction;
		
		if(this.getBrain().getDebugString() == "")
			this.getBrain().setDebugString(this.getFirstAction().getName());

		this.printTrace();
		
	}
	
	/**
	 * TODO : doc
	 */
	public String executePlan() {
		
		String instructionResultat;
		
		//On execute l'action
		instructionResultat = this.actionCourante.executeAction();
		
		//On change d'état si besoin
		ArrayList<WarCondition<AgentAdapterType>> conditions = this.actionCourante.getConditions();
			
		for (WarCondition<AgentAdapterType> conditionCourante : conditions) {
			if(conditionCourante.isValide()){
				this.actionCourante = conditionCourante.getActionDestination();
				this.actionCourante.actionWillBegin();
				break;
			}
		}
		
		if(instructionResultat == null){
			System.err.println("ERREUR l'instruction renvoyée par <" + this.actionCourante.getName() + "> est vide");
			System.exit(0);
		}
		
		return instructionResultat;
	}
	
	public void planWillBegin(){
		//Ici on ne fait rien mais certains plan peuvent en avoir besoin
	}
	
	private void printTrace() {
		if(!this.printTrace)
			return;
		
		System.out.println("\tLe plan contient <" + this.getNom() + "> contient " + this.actions.size() + " actions");
		
		for (WarAction<AgentAdapterType> act : this.actions) {
			System.out.println("\t\tL'action <" + act.getName() + "> contient " + act.getConditions().size() + " conditions de sortie");
			
			for (WarCondition<AgentAdapterType> cond : act.getConditions()) {
				System.out.println("\t\t\tLa condition <" + cond.getClass().getSimpleName() + " a pour destination <" + cond.getActionDestination().getName() + ">");
			}
		}
		
	}
	

	public void addAction(WarAction<AgentAdapterType> a){
		this.actions.add(a);
	}
	
	public AgentAdapterType getBrain(){
		return this.brain;
	}
	
	public String getNom(){
		return this.nom;
	}
	
	public void setFirstAction(WarAction<AgentAdapterType> a){
		this.firstAction = a;
	}
	
	public WarAction<AgentAdapterType> getFirstAction(){
		return this.firstAction;
	}
	
	public void setPrintTrace(boolean b){
		this.printTrace = b;
	}
	
	public ArrayList<WarAction<AgentAdapterType>> getListAction(){
		return this.actions;
	}
	
	public WarPlanSettings getPlanSettings(){
		return planSettings;
	}
	
	private boolean printTrace = false;

}
