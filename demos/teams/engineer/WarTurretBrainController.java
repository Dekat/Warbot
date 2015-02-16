package teams.engineer;

import edu.warbot.agents.agents.WarTurret;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarTurretAdapter;

import java.util.ArrayList;

public class WarTurretBrainController extends WarBrain<WarTurretAdapter> {
	
	private int _sight;
	
	public WarTurretBrainController() {
		super();
		
		_sight = 0;
	}

	@Override
	public String action() {
		
		_sight += 90;
		if(_sight == 360) {
			_sight = 0;
		}
		getAgent().setHeading(_sight);
		
		ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();
		for (WarAgentPercept p : percepts) {
			switch(p.getType()) {
			case WarRocketLauncher :
				if (getAgent().isEnemy(p)) {
					getAgent().setHeading(p.getAngle());
					if (getAgent().isReloaded()) {
						return WarTurret.ACTION_FIRE;
					} else
						return WarTurret.ACTION_RELOAD;
				}
				break;
			default:
				break;
			}
		}
		
		return WarTurret.ACTION_IDLE;
	}
}
