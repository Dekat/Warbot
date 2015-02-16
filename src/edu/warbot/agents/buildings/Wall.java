package edu.warbot.agents.buildings;

import edu.warbot.agents.WarBuilding;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

import java.util.HashMap;

public class Wall extends WarBuilding {

    public static final int COST;
    public static final int MAX_HEALTH;

    static {
		HashMap<String, String> data = WarConfig.getConfigOfWarAgent(WarAgentType.Wall);
        COST = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_COST));
        MAX_HEALTH = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_MAX_HEALTH));
	}

	public Wall(Team team) {
		super(team, WarConfig.getHitboxOfWarAgent(WarAgentType.Wall), COST, MAX_HEALTH);
	}

}
