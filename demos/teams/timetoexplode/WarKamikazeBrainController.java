package teams.timetoexplode;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarKamikaze;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarKamikazeAdapter;
import edu.warbot.communications.WarMessage;

public class WarKamikazeBrainController extends WarBrain<WarKamikazeAdapter> {
	
	public WarKamikazeBrainController() {
		super();
	}

	@Override
	public String action() {
		
		ArrayList<WarMessage> msgs = getAgent().getMessages();
		
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("Enemy base on sight")) {
				getAgent().setHeading(msg.getAngle());
			}
		}
		
		ArrayList<WarPercept> percepts = getAgent().getPercepts();
		
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			case WarBase :
				if (getAgent().isEnemy(p)) {
					return WarKamikaze.ACTION_FIRE;
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
