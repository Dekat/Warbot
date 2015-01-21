package edu.warbot.brains.braincontrollers;

import edu.warbot.brains.WarBrainController;
import edu.warbot.brains.brains.WarTurretBrain;

public abstract class WarTurretAbstractBrainController extends WarBrainController {
	
	public WarTurretAbstractBrainController() {
		super();
	}

	@Override
	public WarTurretBrain getBrain() {
		return (WarTurretBrain) _brain;
	}

}
