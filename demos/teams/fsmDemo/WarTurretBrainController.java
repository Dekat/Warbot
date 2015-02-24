package teams.fsmDemo;

import edu.warbot.agents.agents.WarTurret;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarTurretAdapter;

public class WarTurretBrainController extends WarBrain<WarTurretAdapter> {
	
	public WarTurretBrainController() {
		super();
	}

	@Override
	public String action() {
		return WarTurret.ACTION_IDLE;
	}
}
