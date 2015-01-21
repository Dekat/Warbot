package edu.warbot.agents.enums;

import java.util.ArrayList;

public enum WarAgentType {
	// Unit�s non agressives
	WarExplorer (WarAgentCategory.Worker),
	WarEngineer (WarAgentCategory.Worker),
	
	// Unit�s agressives
	WarRocketLauncher (WarAgentCategory.Soldier),
	WarKamikaze (WarAgentCategory.Soldier),
	WarTurret (WarAgentCategory.Soldier),
	
	// B�timents
	WarBase (WarAgentCategory.Building),
	
	// Projectiles
	WarRocket (WarAgentCategory.Projectile),
	WarBomb (WarAgentCategory.Projectile),
	WarDeathRocket (WarAgentCategory.Projectile),
	
	// Ressources
	WarFood (WarAgentCategory.Resource)
	;
	
	private final WarAgentCategory _category;
	
	private WarAgentType(WarAgentCategory category) {
		_category = category;
	}
	
	public static WarAgentType[] getAgentsOfCategories(WarAgentCategory ... agentCategories) {
		ArrayList<WarAgentType> tmpList = new ArrayList<>();

		for (WarAgentType agentType : WarAgentType.values()) {
			for (WarAgentCategory category : agentCategories) {
				if (agentType.getCategory() == category)
					tmpList.add(agentType);
			}
		}
		
		WarAgentType[] toReturn = new WarAgentType[tmpList.size()];
		for (int i = 0; i < tmpList.size(); i++)
			toReturn[i] = tmpList.get(i);
		
		return toReturn;
	}
	
	public WarAgentCategory getCategory() {
		return _category;
	}
}
