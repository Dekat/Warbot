package edu.warbot.brains.braincontrollers;

import edu.warbot.brains.WarBrainController;
import edu.warbot.brains.brains.WarEngineerBrain;

public abstract class WarEngineerAbstractBrainController extends WarBrainController {
	
	public WarEngineerAbstractBrainController() {
		super();
	}

	@Override
	public WarEngineerBrain getBrain() {
		return (WarEngineerBrain) _brain;
	}
}
