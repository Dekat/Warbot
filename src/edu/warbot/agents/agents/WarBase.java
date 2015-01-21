package edu.warbot.agents.agents;

import java.util.HashMap;

import edu.warbot.agents.CreatorWarAgent;
import edu.warbot.agents.capacities.Creator;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.warbot.brains.brains.WarBaseBrain;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

@SuppressWarnings("serial")
public class WarBase extends CreatorWarAgent implements Creator {

	public static final double ANGLE_OF_VIEW;
	public static final double HITBOX_RADIUS;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfControllableWarAgent("WarBase");
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_BAG_SIZE));
	}
	
	public WarBase(Team team, WarBaseAbstractBrainController brainController) {
		super(ACTION_IDLE, team, HITBOX_RADIUS, brainController, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE);
		
		getBrainController().setBrain(new WarBaseBrain(this));
	}

	@Override
	public boolean isAbleToCreate(WarAgentType agent) {
		if (agent == WarAgentType.WarExplorer || agent == WarAgentType.WarEngineer || agent == WarAgentType.WarKamikaze ||
				agent == WarAgentType.WarRocketLauncher)
			return true;
		return false;
	}
}
