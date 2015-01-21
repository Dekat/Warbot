package edu.warbot.brains.brains;

import edu.warbot.agents.agents.WarTurret;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.capacities.AgressiveBrain;

public class WarTurretBrain extends WarBrain implements AgressiveBrain {
	
	public WarTurretBrain(WarTurret agent) {
		super(agent);
	}

	@Override
	public boolean isReloaded() {
		return ((WarTurret) _agent).isReloaded();
	}

	@Override
	public boolean isReloading() {
		return ((WarTurret) _agent).isReloading();
	}
}
