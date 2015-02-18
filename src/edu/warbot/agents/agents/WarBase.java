package edu.warbot.agents.agents;

import edu.warbot.agents.CreatorWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarBaseAdapter;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarGameConfig;

import java.util.HashMap;

public class WarBase extends CreatorWarAgent {

	public static final double ANGLE_OF_VIEW;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	
	static {
		HashMap<String, String> data = WarGameConfig.getConfigOfWarAgent(WarAgentType.WarBase);
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_BAG_SIZE));
	}
	
	public WarBase(Team team, WarBrain<WarBaseAdapter> brain) {
		super(ACTION_IDLE, team, WarGameConfig.getHitboxOfWarAgent(WarAgentType.WarBase), brain, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE);
		
		brain.setAgentAdapter(new WarBaseAdapter(this));
	}

	@Override
	public boolean isAbleToCreate(WarAgentType agent) {
		if (agent == WarAgentType.WarExplorer || agent == WarAgentType.WarEngineer || agent == WarAgentType.WarKamikaze ||
				agent == WarAgentType.WarRocketLauncher)
			return true;
		return false;
	}
}
