package edu.warbot.brains.adapters;

import edu.warbot.agents.agents.WarKamikaze;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.capacities.Agressive;

public class WarKamikazeAdapter extends MovableWarAgentAdapter implements Agressive {
	
	public WarKamikazeAdapter(WarKamikaze agent) {
		super(agent);
	}
	
	@Override
	protected WarKamikaze getAgent() {
		return (WarKamikaze) super.getAgent();
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