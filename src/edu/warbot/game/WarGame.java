package edu.warbot.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import edu.warbot.agents.WarAgent;
import edu.warbot.launcher.WarGameSettings;
import edu.warbot.maps.AbstractWarMap;
import edu.warbot.maps.DefaultWarMap;

public class WarGame extends Observable implements Observer {

	public static final Color[] TEAM_COLORS = {
		new Color(149, 149, 255), // Blue
		new Color(255, 98, 98), // Red
		Color.YELLOW,
		Color.PINK,
		Color.CYAN,
		Color.ORANGE,
		Color.MAGENTA 
	};

	public static final Integer UPDATE_TEAM_ADDED = 0;
	public static final Integer UPDATE_TEAM_REMOVED = 1;
	public static final Integer GAME_LAUNCHED = 3;
	public static final Integer GAME_STOPPED = 4;
	public static final Integer SIMULATION_CLOSED = 5;
	public static final Integer NEW_TICK = 6;
	
	public static Integer FPS = 0;	
	private double timeLastSecond = -1;
	private Integer currentFPS = 0;

	private MotherNatureTeam _motherNature;
	private List<Team> _playerTeams;
	private AbstractWarMap _map;
	private WarGameSettings settings;
	
	public WarGame(WarGameSettings settings) {
		this.settings = settings;
		this._motherNature = new MotherNatureTeam(this);
		this._playerTeams = settings.getSelectedTeams();
		int colorCounter = 0;
		for(Team t : _playerTeams) {
			t.setColor(TEAM_COLORS[colorCounter]);
			t.setGame(this);
			colorCounter++;
			t.addObserver(this);
		}
		
		// Map creation
		_map = new DefaultWarMap();
	}
	
	public void setLogLevel(Level l) {
		for (Team t : _playerTeams) {
			for (WarAgent a : t.getAllAgents()) {
				a.setLogLevel(l);
			}
		}
		for (WarAgent a : _motherNature.getAllAgents()) {
			a.setLogLevel(l);
		}
		settings.setDefaultLogLevel(l);
	}
	
	public MotherNatureTeam getMotherNatureTeam() {
		return _motherNature;
	}
	
	public void addPlayerTeam(Team team) {
		Team newTeam = Team.duplicate(team, team.getName());
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
	
	public String[] getPlayerTeamNames() {
		String[] toReturn = new String[_playerTeams.size()];
		int compteur = 0;
		for (Team t : _playerTeams) {
			toReturn[compteur] = t.getName();
			compteur++;
		}
		return toReturn;
	}
	
	public AbstractWarMap getMap() {
		return _map;
	}
	
	public WarGameSettings getSettings() {
		return settings;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}
	
	public void doOnEachTick() {
		calculeFPS();
		setChanged();
		notifyObservers(WarGame.NEW_TICK);
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
