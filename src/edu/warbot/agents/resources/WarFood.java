package edu.warbot.agents.resources;

import java.util.HashMap;

import edu.warbot.agents.WarResource;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

public class WarFood extends WarResource {

	public static final int HEALTH_GIVEN;
	public static final double HITBOX_RADIUS;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfWarResource("WarFood");
		HEALTH_GIVEN = Integer.valueOf(data.get(WarConfig.RESOURCE_WARFOOD_CONFIG_HEALTH_GIVEN));
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
	}

	public WarFood(Team team) {
		super(HITBOX_RADIUS, team);
	}
}
