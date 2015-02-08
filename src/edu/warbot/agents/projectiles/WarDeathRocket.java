package edu.warbot.agents.projectiles;

import java.util.HashMap;

import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarProjectile;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

public class WarDeathRocket extends WarProjectile {
	
	public static final double SPEED;
	public static final double EXPLOSION_RADIUS;
	public static final int AUTONOMY;
	public static final int DAMAGE;
	public static final double RANGE;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfWarAgent(WarAgentType.WarDeathRocket);
		SPEED = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_SPEED));
		EXPLOSION_RADIUS = Double.valueOf(data.get(WarConfig.PROJECTILE_CONFIG_EXPLOSION_RADIUS));
		AUTONOMY = Integer.valueOf(data.get(WarConfig.PROJECTILE_CONFIG_AUTONOMY));
		DAMAGE = Integer.valueOf(data.get(WarConfig.PROJECTILE_CONFIG_DAMAGE));		
		RANGE = SPEED * AUTONOMY;
	}

	
	public WarDeathRocket(Team team, WarAgent sender) {
		super(ACTION_MOVE, team, WarConfig.getHitboxOfWarAgent(WarAgentType.WarDeathRocket), sender, SPEED, EXPLOSION_RADIUS, DAMAGE, AUTONOMY);
	}
}
