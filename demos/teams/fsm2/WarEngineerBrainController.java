package teams.fsm2;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarEngineerAdapter;

public class WarEngineerBrainController extends WarBrain<WarEngineerAdapter> {
	
	public WarEngineerBrainController() {
		super();
	}

	@Override
	public String action() {
		
		if (getAgent().isBlocked())
			getAgent().setRandomHeading();
		return WarExplorer.ACTION_MOVE;
	}
}
