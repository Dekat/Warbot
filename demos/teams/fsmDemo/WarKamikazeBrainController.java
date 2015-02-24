package teams.fsmDemo;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarKamikazeAdapter;

import java.util.ArrayList;

public class WarKamikazeBrainController extends WarBrain<WarKamikazeAdapter> {
	
	public WarKamikazeBrainController() {
		super();
	}

	@Override
	public String action() {
		ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();
		
		for (WarAgentPercept p : percepts) {
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
