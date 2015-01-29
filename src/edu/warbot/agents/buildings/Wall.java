package edu.warbot.agents.buildings;

import java.util.HashMap;

import edu.warbot.agents.WarBuilding;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

@SuppressWarnings("serial")
public class Wall extends WarBuilding {

	public static final double HITBOX_RADIUS;

	static {
		HashMap<String, String> data = WarConfig.getConfigOfWarBuildings("Wall");
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
	}

	public Wall(Team team, double hitboxRadius) {
		super(team, hitboxRadius);
	}

}
