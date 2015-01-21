package edu.warbot.brains.braincontrollers;

import edu.warbot.brains.WarBrainController;
import edu.warbot.brains.brains.WarKamikazeBrain;

public abstract class WarKamikazeAbstractBrainController extends WarBrainController {
	
	public WarKamikazeAbstractBrainController() {
		super();
	}

	@Override
	public WarKamikazeBrain getBrain() {
		return (WarKamikazeBrain) _brain;
	}

}
