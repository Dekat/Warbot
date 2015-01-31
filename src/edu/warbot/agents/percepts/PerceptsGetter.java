package edu.warbot.agents.percepts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.WarGame;

public abstract class PerceptsGetter {

	private ControllableWarAgent _agent;
	private WarGame game;

	boolean _arePerceptsAlreadyCatchedThisTick;

	private ArrayList<WarPercept> _enemies;
	private ArrayList<WarPercept> _allies;
	private ArrayList<WarPercept> _resources;

	public PerceptsGetter(ControllableWarAgent agent, WarGame game) {
		_agent = agent;
		this.game = game;
	}

	protected ControllableWarAgent getAgent() {
		return _agent;
	}
	
	protected WarGame getGame() {
		return game;
	}

	public abstract ArrayList<WarPercept> getPercepts();

	public ArrayList<WarPercept> getAgentsPercepts(boolean ally) {
		if(! _arePerceptsAlreadyCatchedThisTick)
			this.getAllPerceptsAndSortThem();

		if(ally)
			return this._allies;
		else
			return this._enemies;
	}

	public ArrayList<WarPercept> getResourcesPercepts() {
		if(! _arePerceptsAlreadyCatchedThisTick)
			this.getAllPerceptsAndSortThem();

		return this._resources;
	}

	public ArrayList<WarPercept> getPerceptsByType(WarAgentType agentType, boolean ally){
		if(!this._arePerceptsAlreadyCatchedThisTick)
			this.getAllPerceptsAndSortThem();

		ArrayList<WarPercept> perceptRes = new ArrayList<>();
		ArrayList<WarPercept> listePerceptToParcours;

		if(ally)
			listePerceptToParcours = this._allies;
		else
			listePerceptToParcours = this._enemies;

		for (WarPercept warPercept : listePerceptToParcours) {
			if(warPercept.getType().equals(agentType)){
				perceptRes.add(warPercept);
			}
		}

		return perceptRes;
	}

	private void getAllPerceptsAndSortThem() {
		this._allies = new ArrayList<>();
		this._enemies = new ArrayList<>();
		this._resources = new ArrayList<>();

		for (WarPercept perceptCourant : this.getPercepts()) {
			if(perceptCourant.getTeamName().equals(game.getMotherNatureTeam().getName()))
				this._resources.add(perceptCourant);
			else if(perceptCourant.getTeamName().equals(_agent.getTeam().getName()))
				this._allies.add(perceptCourant);
			else
				this._enemies.add(perceptCourant);
		}

		//Tri les percepts
		Collections.sort(((List<WarPercept>)this._enemies));
		Collections.sort(((List<WarPercept>)this._allies));
		Collections.sort(((List<WarPercept>)this._resources));
		
		this._arePerceptsAlreadyCatchedThisTick = true;
	}

	public void setPerceptsAlreadyInit(boolean b){
		this._arePerceptsAlreadyCatchedThisTick = b;
	}

}