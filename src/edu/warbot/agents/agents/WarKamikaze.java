package edu.warbot.agents.agents;

import java.util.HashMap;
import java.util.logging.Level;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.capacities.Agressive;
import edu.warbot.agents.projectiles.WarBomb;
import edu.warbot.brains.braincontrollers.WarKamikazeAbstractBrainController;
import edu.warbot.brains.brains.WarKamikazeBrain;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

@SuppressWarnings("serial")
public class WarKamikaze extends MovableWarAgent implements Agressive {

	public static final double ANGLE_OF_VIEW;
	public static final double HITBOX_RADIUS;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	public static final double SPEED;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfControllableWarAgent("WarKamikaze");
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_BAG_SIZE));
		SPEED = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_SPEED));
	}
	
	public WarKamikaze(Team team, WarKamikazeAbstractBrainController brainController) {
		super(ACTION_IDLE, team, HITBOX_RADIUS, brainController, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE, SPEED);
		
		getBrainController().setBrain(new WarKamikazeBrain(this));
	}

	@Override
	public String fire() {
		logger.log(Level.FINER, this.toString() + " fired.");
		launchAgent(new WarBomb(getTeam(), this));
		killAgent(this);
		return getBrainController().action();
	}

	@Override
	public String beginReloadWeapon() {
		return getBrainController().action();
	}

	@Override
	public boolean isReloaded() {
		return true;
	}

	@Override
	public boolean isReloading() {
		return false;
	}

	
}
