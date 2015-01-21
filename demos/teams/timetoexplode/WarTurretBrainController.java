package teams.timetoexplode;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.agents.WarTurret;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.braincontrollers.WarTurretAbstractBrainController;

public class WarTurretBrainController extends WarTurretAbstractBrainController {
	
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
		getBrain().setHeading(_sight);
		
		ArrayList<WarPercept> percepts = getBrain().getPercepts();	
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			default:
				if (getBrain().isEnemy(p)) {
					getBrain().setHeading(p.getAngle());
					if (getBrain().isReloaded()) {
						return WarTurret.ACTION_FIRE;
					} else
						return WarTurret.ACTION_RELOAD;
				}
				break;
			}
		}
		
		return WarTurret.ACTION_IDLE;
	}
}
