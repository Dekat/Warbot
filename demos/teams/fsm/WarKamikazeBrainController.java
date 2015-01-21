package teams.fsm;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.braincontrollers.WarKamikazeAbstractBrainController;

public class WarKamikazeBrainController extends WarKamikazeAbstractBrainController {
	
	public WarKamikazeBrainController() {
		super();
	}

	@Override
	public String action() {
		ArrayList<WarPercept> percepts = getBrain().getPercepts();
		
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			case WarBase :
				if (getBrain().isEnemy(p)) {
					getBrain().broadcastMessageToAll("Ennemi Base Found", String.valueOf(p.getAngle()), String.valueOf(p.getDistance()));
				}
				break;
			default:
				break;
			}
		}
		
		if (getBrain().isBlocked())
			getBrain().setRandomHeading();
		return WarExplorer.ACTION_MOVE;
	}
}
