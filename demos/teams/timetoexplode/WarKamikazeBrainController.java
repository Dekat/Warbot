package teams.timetoexplode;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarKamikaze;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.braincontrollers.WarKamikazeAbstractBrainController;
import edu.warbot.communications.WarMessage;

public class WarKamikazeBrainController extends WarKamikazeAbstractBrainController {
	
	public WarKamikazeBrainController() {
		super();
	}

	@Override
	public String action() {
		
		ArrayList<WarMessage> msgs = getBrain().getMessages();
		
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("Enemy base on sight")) {
				getBrain().setHeading(msg.getAngle());
			}
		}
		
		ArrayList<WarPercept> percepts = getBrain().getPercepts();
		
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			case WarBase :
				if (getBrain().isEnemy(p)) {
					return WarKamikaze.ACTION_FIRE;
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
