package edu.warbot.agents.projectiles;

import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarProjectile;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarGameConfig;

import java.util.HashMap;

public class WarDeathRocket extends WarProjectile {
	
	public static final double SPEED;
	public static final double EXPLOSION_RADIUS;
	public static final int AUTONOMY;
	public static final int DAMAGE;
	public static final double RANGE;
	
	static {
		HashMap<String, String> data = WarGameConfig.getConfigOfWarAgent(WarAgentType.WarDeathRocket);
		SPEED = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_SPEED));
		EXPLOSION_RADIUS = Double.valueOf(data.get(WarGameConfig.PROJECTILE_CONFIG_EXPLOSION_RADIUS));
		AUTONOMY = Integer.valueOf(data.get(WarGameConfig.PROJECTILE_CONFIG_AUTONOMY));
		DAMAGE = Integer.valueOf(data.get(WarGameConfig.PROJECTILE_CONFIG_DAMAGE));
		RANGE = SPEED * AUTONOMY;
	}

	
	public WarDeathRocket(Team team, WarAgent sender) {
		super(ACTION_MOVE, team, WarGameConfig.getHitboxOfWarAgent(WarAgentType.WarDeathRocket), sender, SPEED, EXPLOSION_RADIUS, DAMAGE, AUTONOMY);
	}
}
