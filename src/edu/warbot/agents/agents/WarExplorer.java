package edu.warbot.agents.agents;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarExplorerAdapter;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarGameConfig;

import java.util.HashMap;

public class WarExplorer extends MovableWarAgent {

	public static final double ANGLE_OF_VIEW;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	public static final double SPEED;
	
	static {
		HashMap<String, String> data = WarGameConfig.getConfigOfWarAgent(WarAgentType.WarExplorer);
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_BAG_SIZE));
		SPEED = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_SPEED));
	}
	
	public WarExplorer(Team team, WarBrain<WarExplorerAdapter> brain) {
		super(ACTION_IDLE, team, WarGameConfig.getHitboxOfWarAgent(WarAgentType.WarExplorer), brain, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE, SPEED);
		
		brain.setAgentAdapter(new WarExplorerAdapter(this));
	}

}
