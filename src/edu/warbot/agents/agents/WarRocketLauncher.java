package edu.warbot.agents.agents;

import java.util.HashMap;
import java.util.logging.Level;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.capacities.Agressive;
import edu.warbot.agents.projectiles.WarRocket;
import edu.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.warbot.brains.brains.WarRocketLauncherBrain;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

@SuppressWarnings("serial")
public class WarRocketLauncher extends MovableWarAgent implements Agressive {

	public static final double ANGLE_OF_VIEW;
	public static final double HITBOX_RADIUS;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	public static final double SPEED;
	public static final int	TICKS_TO_RELOAD;

	private boolean _reloaded;
	private boolean _reloading;
	private int _tickLeftBeforeReloaded; // Retient le tick global quand le reload a commencé
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfControllableWarAgent("WarRocketLauncher");
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_BAG_SIZE));
		SPEED = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_SPEED));
		TICKS_TO_RELOAD = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_TICKS_TO_RELOAD));
	}

	public WarRocketLauncher(Team team, WarRocketLauncherAbstractBrainController brainController) {
		super(ACTION_IDLE, team, HITBOX_RADIUS, brainController, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE, SPEED);
		
		getBrainController().setBrain(new WarRocketLauncherBrain(this));
		_tickLeftBeforeReloaded = TICKS_TO_RELOAD;
		_reloaded = false;
		_reloading = true;
	}

	@Override
	protected void doOnEachTick() {
		_tickLeftBeforeReloaded--;
		if (_tickLeftBeforeReloaded <= 0 && _reloading) {
			_reloaded = true;
			_reloading = false;
		}
		super.doOnEachTick();
	}
	
	@Override
	public String fire() {
		logger.log(Level.FINEST, this.toString() + " firing...");
		if (isReloaded()) {
			logger.log(Level.FINER, this.toString() + " fired.");
			launchAgent(new WarRocket(getTeam(), this));
			_reloaded = false;
		}
		return getBrainController().action();
	}

	@Override
	public String beginReloadWeapon() {
		logger.log(Level.FINEST, this.toString() + " begin reload weapon.");
		if (! _reloading) {
			_tickLeftBeforeReloaded = TICKS_TO_RELOAD;
			_reloading = true;
		}
		return getBrainController().action();
	}

	@Override
	public boolean isReloaded() {
		return _reloaded;
	}

	@Override
	public boolean isReloading() {
		return _reloading;
	}
}
