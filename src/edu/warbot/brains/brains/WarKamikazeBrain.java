package edu.warbot.brains.brains;

import edu.warbot.agents.agents.WarKamikaze;
import edu.warbot.brains.MovableWarAgentBrain;
import edu.warbot.brains.capacities.AgressiveBrain;

public class WarKamikazeBrain extends MovableWarAgentBrain implements AgressiveBrain {
	
	public WarKamikazeBrain(WarKamikaze agent) {
		super(agent);
	}

	@Override
	public boolean isReloaded() {
		return ((WarKamikaze) _agent).isReloaded();
	}

	@Override
	public boolean isReloading() {
		return ((WarKamikaze) _agent).isReloading();
	}
}