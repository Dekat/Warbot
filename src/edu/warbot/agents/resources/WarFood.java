package edu.warbot.agents.resources;

import edu.warbot.agents.WarResource;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarGameConfig;

import java.util.HashMap;

public class WarFood extends WarResource {

	public static final int HEALTH_GIVEN;

	static {
		HashMap<String, String> data = WarGameConfig.getConfigOfWarAgent(WarAgentType.WarFood);
		HEALTH_GIVEN = Integer.valueOf(data.get(WarGameConfig.RESOURCE_WARFOOD_CONFIG_HEALTH_GIVEN));
	}

	public WarFood(Team team) {
		super(WarGameConfig.getHitboxOfWarAgent(WarAgentType.WarFood), team);
	}
}
