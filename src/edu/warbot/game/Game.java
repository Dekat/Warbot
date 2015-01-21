package edu.warbot.game;

import java.awt.Color;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarResource;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.WarBrainController;
import edu.warbot.launcher.Simulation;
import edu.warbot.maps.WarMap;
import edu.warbot.tools.WarCircle;

public class Game extends Observable implements Observer {

	public static final Integer UPDATE_TEAM_ADDED = 0;
	public static final Integer UPDATE_TEAM_REMOVED = 1;
	public static final Integer GAME_LAUNCHED = 3;
	public static final Integer GAME_STOPPED = 4;
	public static final Integer SIMULATION_CLOSED = 5;
	public static final Integer NEW_TICK = 6;
	
	public static Integer FPS = 0;	
	private double timeLastSecond = -1;
	private Integer currentFPS = 0;

	private static Game _instance;

	private MotherNatureTeam _motherNature;
	private ArrayList<Team> _playerTeams;
	private WarMap _map;
	
	private Game() {
		this._motherNature = new MotherNatureTeam();
		this._playerTeams = new ArrayList<>();
		
		// Map creation
		_map = new WarMap(new Dimension(1000, 600));
		int teamsPositionsRadius = 100;
		_map.addTeamPosition(new WarCircle(_map.getWidth() - teamsPositionsRadius, teamsPositionsRadius, teamsPositionsRadius));
		_map.addTeamPosition(new WarCircle(teamsPositionsRadius, _map.getHeight() - teamsPositionsRadius, teamsPositionsRadius));
		int foodPositionsRadius = 200;
		_map.addFoodPosition(new WarCircle(_map.getWidth() - foodPositionsRadius, foodPositionsRadius, foodPositionsRadius));
		_map.addFoodPosition(new WarCircle(foodPositionsRadius, _map.getHeight() - foodPositionsRadius, foodPositionsRadius));
		_map.addFoodPosition(new WarCircle(_map.getWidth() / 2, _map.getHeight() / 2, foodPositionsRadius));
		_map.addFoodPosition(new WarCircle(_map.getWidth() / 2, _map.getHeight() / 2, foodPositionsRadius));
	}
	
	public void setLevel(Level l) {
		for (Team t : _playerTeams) {
			for (WarAgent a : t.getAllAgents()) {
				a.setLogLevel(l);
			}
		}
		for (WarAgent a : _motherNature.getAllAgents()) {
			a.setLogLevel(l);
		}
		Simulation.getInstance().setDefaultLogLevel(l);
	}
	
	public static Game getInstance() {
		if (_instance == null)
			_instance = new Game();
		return _instance;
	}
	
	public MotherNatureTeam getMotherNatureTeam() {
		return _motherNature;
	}
	
	public void addPlayerTeam(Team team, Color color) {
		Team newTeam = Team.duplicate(team, team.getName());
		newTeam.setColor(color);
		_playerTeams.add(newTeam);
		
		newTeam.addObserver(this);
		setChanged();
		notifyObservers(UPDATE_TEAM_ADDED);
	}

	public void removePlayerTeam(Team team) {
		team.destroy();
		_playerTeams.remove(team);
		
		team.deleteObserver(this);
		setChanged();
		notifyObservers(UPDATE_TEAM_REMOVED);
	}
	
	public Team getPlayerTeam(String teamName) {
		for (Team t : _playerTeams) {
			if (t.getName().equals(teamName))
				return t;
		}
		return null;
	}

	public ArrayList<Team> getPlayerTeams() {
		return new ArrayList<Team>(_playerTeams);
	}
	
	public ArrayList<Team> getAllTeams() {
		ArrayList<Team> teams = getPlayerTeams();
		teams.add(getMotherNatureTeam());
		return teams;
	}
	
	public ArrayList<WarAgent> getAllAgentsInRadiusOf(WarAgent a, double radius) {
		ArrayList<WarAgent> toReturn = new ArrayList<>();
		for (Team t : _playerTeams) {
			toReturn.addAll(t.getAllAgentsInRadiusOf(a, radius));
		}
		toReturn.addAll(_motherNature.getAllAgentsInRadiusOf(a, radius));
		return toReturn;
	}
	
	public ArrayList<WarAgent> getAllAgentsInRadius(double posX, double posY, double radius) {
		ArrayList<WarAgent> toReturn = new ArrayList<>();
		for (Team t : _playerTeams) {
			toReturn.addAll(t.getAllAgentsInRadius(posX, posY, radius));
		}
		toReturn.addAll(_motherNature.getAllAgentsInRadius(posX, posY, radius));
		return toReturn;
	}
	
	public void restartNewGame() {
		for (Team t : _playerTeams)
			for (WarAgent a : t.getAllAgents())
				a.killAgent(a);
		for (WarAgent a : _motherNature.getAllAgents())
			a.killAgent(a);
		this._motherNature = new MotherNatureTeam();
		this._playerTeams.clear();
	}

	public static ControllableWarAgent instantiateNewControllableWarAgent(String agentName, Team team) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		String agentToCreateClassName = WarBase.class.getPackage().getName() + "." + agentName;
		Class<? extends WarBrainController> brainControllerClass = team.getBrainControllerOfAgent(agentName);
		ControllableWarAgent a = (ControllableWarAgent) Class.forName(agentToCreateClassName).getConstructor(
				Team.class, Class.forName(brainControllerClass.getSuperclass().getName())).newInstance(
						team, brainControllerClass.newInstance());
		return a;
	}
		
	public static WarResource instantiateNewWarResource(String agentName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		String resourceToCreateClassName = WarFood.class.getPackage().getName() + "." + agentName;
		WarResource a = (WarResource) Class.forName(resourceToCreateClassName).newInstance();
		return a;
	}
		
	public String[] getPlayerTeamNames() {
		String[] toReturn = new String[_playerTeams.size()];
		int compteur = 0;
		for (Team t : _playerTeams) {
			toReturn[compteur] = t.getName();
			compteur++;
		}
		return toReturn;
	}
	
	public WarMap getMap() {
		return _map;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}
	
	public void doOnEachTick() {
		calculeFPS();
		setChanged();
		notifyObservers(Game.NEW_TICK);
		for (Team t : _playerTeams)
			t.doOnEachTick();
		_motherNature.doOnEachTick();
	}

	public void stopTheGame() {
		setChanged();
		notifyObservers(GAME_STOPPED);
	}
	
	public void setSimulationClosed() {
		setChanged();
		notifyObservers(SIMULATION_CLOSED);
	}
	
	public void setGameStarted() {
		setChanged();
		notifyObservers(GAME_LAUNCHED);
	}
	
	private void calculeFPS() {
		currentFPS++;
		if(timeLastSecond + 1000 < System.currentTimeMillis() || timeLastSecond == -1){
			timeLastSecond = System.currentTimeMillis();
			FPS = currentFPS;
			currentFPS = 0;
		}
	}
	
	public Integer getFPS() {
		return FPS;
	}
}
