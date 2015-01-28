package teams.fsm;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarKamikazeAdapter;

public class WarKamikazeBrainController extends WarBrain<WarKamikazeAdapter> {
	
	public WarKamikazeBrainController() {
		super();
	}

	@Override
	public String action() {
		ArrayList<WarPercept> percepts = getAgent().getPercepts();
		
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			case WarBase :
				if (getAgent().isEnemy(p)) {
					getAgent().broadcastMessageToAll("Ennemi Base Found", String.valueOf(p.getAngle()), String.valueOf(p.getDistance()));
				}
				break;
			default:
				break;
			}
		}
		
		if (getAgent().isBlocked())
			getAgent().setRandomHeading();
		return WarExplorer.ACTION_MOVE;
	}
}
