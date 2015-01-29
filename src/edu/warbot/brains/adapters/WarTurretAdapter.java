package edu.warbot.brains.adapters;

import edu.warbot.agents.agents.WarTurret;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.capacities.Agressive;

public class WarTurretAdapter extends ControllableWarAgentAdapter implements Agressive {
	
	public WarTurretAdapter(WarTurret agent) {
		super(agent);
	}
	
	@Override
	protected WarTurret getAgent() {
		return (WarTurret) super.getAgent();
	}

	@Override
	public boolean isReloaded() {
		return getAgent().isReloaded();
	}

	@Override
	public boolean isReloading() {
		return getAgent().isReloading();
	}
}
