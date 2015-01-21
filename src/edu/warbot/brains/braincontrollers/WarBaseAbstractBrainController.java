package edu.warbot.brains.braincontrollers;

import edu.warbot.brains.WarBrainController;
import edu.warbot.brains.brains.WarBaseBrain;

public abstract class WarBaseAbstractBrainController extends WarBrainController {

	public WarBaseAbstractBrainController() {
		super();
	}

	@Override
	public WarBaseBrain getBrain() {
		return (WarBaseBrain) _brain;
	}

}