package teams.timetoexplode;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarKamikaze;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarKamikazeAdapter;
import edu.warbot.communications.WarMessage;

import java.util.ArrayList;

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
		
		ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();
		
		for (WarAgentPercept p : percepts) {
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
