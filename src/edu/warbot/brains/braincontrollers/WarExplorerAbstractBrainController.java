package edu.warbot.brains.braincontrollers;

import edu.warbot.brains.WarBrainController;
import edu.warbot.brains.brains.WarExplorerBrain;

public abstract class WarExplorerAbstractBrainController extends WarBrainController {

	public WarExplorerAbstractBrainController() {
		super();
	}

	@Override
	public WarExplorerBrain getBrain() {
		return (WarExplorerBrain) _brain;
	}

}
