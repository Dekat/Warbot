package edu.warbot.brains.adapters;

import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.capacities.Agressive;

public class WarRocketLauncherAdapter extends MovableWarAgentAdapter implements Agressive {
	
	public WarRocketLauncherAdapter(WarRocketLauncher agent) {
		super(agent);
	}
	
	@Override
	protected WarRocketLauncher getAgent() {
		return (WarRocketLauncher) super.getAgent();
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
