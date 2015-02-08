package edu.warbot.agents.agents;

import java.util.HashMap;
import java.util.logging.Level;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.actions.AgressiveActions;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.projectiles.WarBomb;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarKamikazeAdapter;
import edu.warbot.brains.capacities.Agressive;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

public class WarKamikaze extends MovableWarAgent implements AgressiveActions, Agressive {

	public static final double ANGLE_OF_VIEW;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	public static final double SPEED;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfWarAgent(WarAgentType.WarKamikaze);
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_BAG_SIZE));
		SPEED = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_SPEED));
	}
	
	public WarKamikaze(Team team, WarBrain<WarKamikazeAdapter> brain) {
		super(ACTION_IDLE, team, WarConfig.getHitboxOfWarAgent(WarAgentType.WarKamikaze), brain, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE, SPEED);
		
		brain.setAgentAdapter(new WarKamikazeAdapter(this));
	}

	@Override
	public String fire() {
		logger.log(Level.FINER, this.toString() + " fired.");
		launchAgent(new WarBomb(getTeam(), this));
		killAgent(this);
		return getBrain().action();
	}

	@Override
	public String beginReloadWeapon() {
		return getBrain().action();
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
