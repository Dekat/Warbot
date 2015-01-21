package edu.warbot.brains.brains;

import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.brains.MovableWarAgentBrain;
import edu.warbot.brains.capacities.AgressiveBrain;

public class WarRocketLauncherBrain extends MovableWarAgentBrain implements AgressiveBrain {
	
	public WarRocketLauncherBrain(WarRocketLauncher agent) {
		super(agent);
	}

	@Override
	public boolean isReloaded() {
		return ((WarRocketLauncher) _agent).isReloaded();
	}

	@Override
	public boolean isReloading() {
		return ((WarRocketLauncher) _agent).isReloading();
	}

}
