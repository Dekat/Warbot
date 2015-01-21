package teams.fsm;

import edu.warbot.agents.agents.WarTurret;
import edu.warbot.brains.braincontrollers.WarTurretAbstractBrainController;

public class WarTurretBrainController extends WarTurretAbstractBrainController {
	
	public WarTurretBrainController() {
		super();
	}

	@Override
	public String action() {
		return WarTurret.ACTION_IDLE;
	}
}
