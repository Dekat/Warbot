package teams.timetoexplode;

import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarEngineerAdapter;

public class WarEngineerBrainController extends WarBrain<WarEngineerAdapter> {
	
	public WarEngineerBrainController() {
		super();
	}

	@Override
	public String action() {
		
		return WarEngineer.ACTION_IDLE;
	}
}
