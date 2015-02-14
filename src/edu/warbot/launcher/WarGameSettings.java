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
import edu.warbot.game.mode.AbstractGameMode;
import edu.warbot.game.mode.endCondition.AbstractEndCondition;
import edu.warbot.maps.AbstractWarMap;
import edu.warbot.maps.DefaultWarMap;

public class WarGameSettings {
	
	private Map<WarAgentType, Integer> _nbAgentOfEachType;
	private WarGameMode _gameMode;
    private Object[] gameModeArguments;
	private Level _defaultLogLevel;
	private int _foodAppearanceRate;
	private Class<? extends PerceptsGetter> _perceptsGetter;
	private boolean _isEnabledEnhancedGraphism;
	private List<Team> selectedTeams;
	private SituationLoader situationLoader;
    private AbstractWarMap selectedMap;

	public WarGameSettings() {
		this._nbAgentOfEachType = new HashMap<>();
		this.selectedTeams = new ArrayList<>();

		restartParameters();
	}
	
	private void restartParameters() {
		for (WarAgentType a : WarAgentType.values()) {
			_nbAgentOfEachType.put(a, WarConfig.getNbAgentsAtStartOfType(a.toString()));
		}
		_gameMode = WarGameMode.Duel;
        gameModeArguments = new Object[]{};
		_defaultLogLevel = WarConfig.getLoggerLevel();
		_foodAppearanceRate = WarConfig.getFoodAppearanceRate();
		_perceptsGetter = WarConfig.getDefaultPerception();
		_isEnabledEnhancedGraphism = false; // TODO set 3D as alternative viewer
        this.selectedMap = new DefaultWarMap();
	}
	
	public void setNbAgentOfType(WarAgentType agent, int number) {
		_nbAgentOfEachType.put(agent, number);
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

    public AbstractWarMap getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(AbstractWarMap selectedMap) {
        this.selectedMap = selectedMap;
    }

    public void setGameModeArguments(Object[] gameModeArguments) {
        this.gameModeArguments = gameModeArguments;
    }

    public Object[] getGameModeArguments() {
        return gameModeArguments;
    }
}
