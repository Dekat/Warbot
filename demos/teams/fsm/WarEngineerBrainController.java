package teams.fsm;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.brains.braincontrollers.WarEngineerAbstractBrainController;

public class WarEngineerBrainController extends WarEngineerAbstractBrainController {
	
	public WarEngineerBrainController() {
		super();
	}

	@Override
	public String action() {
		
		if (getBrain().isBlocked())
			getBrain().setRandomHeading();
		return WarExplorer.ACTION_MOVE;
	}
}
