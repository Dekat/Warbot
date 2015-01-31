package edu.warbot.launcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.InRadiusPerceptsGetter;
import edu.warbot.agents.percepts.PerceptsGetter;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.game.WarGameMode;

public class WarGameSettings {
	
	private Map<WarAgentType, Integer> _nbAgentOfEachType;
	private WarGameMode _gameMode;
	private Level _defaultLogLevel;
	private int _foodAppearanceRate;
	private double _maxHitboxRadius;
	private boolean _isOpenWorld;
	private Class<? extends PerceptsGetter> _perceptsGetter;
	private boolean _isEnabledEnhancedGraphism;
	private List<Team> selectedTeams;
	private SituationLoader situationLoader;

	public WarGameSettings() {
		this._nbAgentOfEachType = new HashMap<WarAgentType, Integer>();
		this.selectedTeams = new ArrayList<Team>();
		
		restartParameters();
	}
	
	private void restartParameters() {
		for (WarAgentType a : WarAgentType.values()) {
			_nbAgentOfEachType.put(a, WarConfig.getNbAgentsAtStartOfType(a.toString()));
		}
		_gameMode = WarGameMode.Duel;
		_defaultLogLevel = WarConfig.getLoggerLevel();
		_foodAppearanceRate = WarConfig.getFoodAppearanceRate();
		_maxHitboxRadius = WarConfig.getMaxHitBoxRadius();
		_perceptsGetter = WarConfig.getDefaultPerception();
		_isOpenWorld = WarConfig.isOpenWorld();
		_isEnabledEnhancedGraphism = false; // TODO add it to config file
	}
	
	public void setNbAgentOfType(WarAgentType agent, int nombre) {
		_nbAgentOfEachType.put(agent, nombre);
	}
	
	public int getNbAgentOfType(WarAgentType agent) {
		return _nbAgentOfEachType.get(agent);
	}

	public WarGameMode getGameMode() {
		return _gameMode;
	}

	public void setGameMode(WarGameMode gameMode) {
		_gameMode = gameMode;
	}

	public int getFoodAppearanceRate() {
		return this._foodAppearanceRate;
	}
	
	public void setFoodAppearanceRate(int rate) {
		_foodAppearanceRate = rate;
	}
	
	public void setDefaultLogLevel(Level level) {
		_defaultLogLevel = level;
	}
	
	public Level getLogLevel() {
		return _defaultLogLevel;
	}
	
	public double getMaxHitboxRadius() {
		return _maxHitboxRadius;
	}
	
	public Class<? extends PerceptsGetter> getPerceptsGetterClass() {
		return _perceptsGetter;
	}
	
	public void setPerceptsGetterClass(Class<? extends PerceptsGetter> perceptsGetter) {
		_perceptsGetter = perceptsGetter;
	}
	
	public PerceptsGetter getPerceptsGetterNewInstance(ControllableWarAgent agent, WarGame game) {
		try {
			return _perceptsGetter.getConstructor(ControllableWarAgent.class, WarGame.class).newInstance(agent, game);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.err.println("La classe " + _perceptsGetter.getName() + " ne peut pas être instanciée. InRadiusPerceptsGetter pris à la place.");
			e.printStackTrace();
			return new InRadiusPerceptsGetter(agent, game);
		}
	}
	
	public boolean isOpenWorld() {
		return _isOpenWorld;
	}
	
	public void setOpenWorld(boolean bool) {
		_isOpenWorld = bool;
	}
	
	public boolean isEnabledEnhancedGraphism() {
		return _isEnabledEnhancedGraphism;
	}
	
	public void setEnabledEnhancedGraphism(boolean bool) {
		_isEnabledEnhancedGraphism = bool;
	}
	
	public List<Team> getSelectedTeams() {
		return selectedTeams;
	}
	
	public void addSelectedTeam(Team team) {
		selectedTeams.add(team);
	}
	
	public void prepareForNewGame() {
		for(Team t : selectedTeams)
			t.removeAllAgents();
		selectedTeams.clear();
		situationLoader = null;
	}

	public SituationLoader getSituationLoader() {
		return situationLoader;
	}

	public void setSituationLoader(SituationLoader situationLoader) {
		this.situationLoader = situationLoader;
	}
	
}