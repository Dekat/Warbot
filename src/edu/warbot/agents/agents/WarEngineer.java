package edu.warbot.agents.agents;

import java.util.HashMap;

import edu.warbot.agents.CreatorWarAgent;
import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.capacities.Creator;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.braincontrollers.WarEngineerAbstractBrainController;
import edu.warbot.brains.brains.WarEngineerBrain;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

@SuppressWarnings("serial")
public class WarEngineer extends MovableWarAgent implements Creator {
	
	public static final double ANGLE_OF_VIEW;
	public static final double HITBOX_RADIUS;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	public static final double SPEED;
	
	private WarAgentType _nextAgentToCreate;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfControllableWarAgent("WarEngineer");
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_BAG_SIZE));
		SPEED = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_SPEED));
	}
	
	public WarEngineer(Team team, WarEngineerAbstractBrainController brainController) {
		super(ACTION_IDLE, team, HITBOX_RADIUS, brainController, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE, SPEED);
		
		getBrainController().setBrain(new WarEngineerBrain(this));
	}

	@Override
	public String create() {
		CreatorWarAgent.defaultCreateUnit(this, _nextAgentToCreate);
		return getBrainController().action();
	}

	@Override
	public void setNextAgentToCreate(WarAgentType nextAgentToCreate) {
		_nextAgentToCreate = nextAgentToCreate;
	}

	@Override
	public WarAgentType getNextAgentToCreate() {
		return _nextAgentToCreate;
	}

	@Override
	public boolean isAbleToCreate(WarAgentType agent) {
		if (agent == WarAgentType.WarTurret)
			return true;
		return false;
	}
	
}