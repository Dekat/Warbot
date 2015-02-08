package edu.warbot.agents.buildings;

import java.util.HashMap;

import edu.warbot.agents.WarBuilding;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

public class Wall extends WarBuilding {

//	static {
//		HashMap<String, String> data = WarConfig.getConfigOfWarAgent(WarAgentType.Wall);
//		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
//	}

	public Wall(Team team, double hitboxRadius) {
		super(team, WarConfig.getHitboxOfWarAgent(WarAgentType.Wall));
	}

}
