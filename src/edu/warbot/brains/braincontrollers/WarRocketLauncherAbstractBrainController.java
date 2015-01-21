package edu.warbot.brains.braincontrollers;

import edu.warbot.brains.WarBrainController;
import edu.warbot.brains.brains.WarRocketLauncherBrain;

public abstract class WarRocketLauncherAbstractBrainController extends WarBrainController {

	public WarRocketLauncherAbstractBrainController() {
		super();
	}

	@Override
	public WarRocketLauncherBrain getBrain() {
		return (WarRocketLauncherBrain) _brain;
	}
	
}