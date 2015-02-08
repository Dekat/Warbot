package edu.warbot.agents.resources;

import java.util.HashMap;

import edu.warbot.agents.WarResource;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

public class WarFood extends WarResource {

	public static final int HEALTH_GIVEN;

	static {
		HashMap<String, String> data = WarConfig.getConfigOfWarAgent(WarAgentType.WarFood);
		HEALTH_GIVEN = Integer.valueOf(data.get(WarConfig.RESOURCE_WARFOOD_CONFIG_HEALTH_GIVEN));
	}

	public WarFood(Team team) {
		super(WarConfig.getHitboxOfWarAgent(WarAgentType.WarFood), team);
	}
}
