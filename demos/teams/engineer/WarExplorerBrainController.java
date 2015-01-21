package teams.engineer;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.warbot.communications.WarMessage;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private boolean _starving;
	
	public WarExplorerBrainController() {
		super();
		
		_starving = false;
	}

	@Override
	public String action() {
		ArrayList<WarPercept> percepts = getBrain().getPercepts();
		
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			case WarFood :
				if(p.getDistance() < WarFood.MAX_DISTANCE_TAKE && !getBrain().isBagFull()) {
					getBrain().setHeading(p.getAngle());
					return WarExplorer.ACTION_TAKE;
				}else if(!getBrain().isBagFull()){
					getBrain().setHeading(p.getAngle());
				}
				break;
			case WarBase :
				if(getBrain().isEnemy(p)) {
					getBrain().broadcastMessageToAll("Enemy base on sight", String.valueOf(p.getAngle()), String.valueOf(p.getDistance()));
				}
				break;
			case WarEngineer :
				if(p.getDistance() < WarEngineer.MAX_DISTANCE_GIVE && getBrain().getNbElementsInBag() > 0) {
					getBrain().setDebugString("Giving food");
					getBrain().setIdNextAgentToGive(p.getID());
					return WarExplorer.ACTION_GIVE;
				}
				if(getBrain().isBagEmpty()) {
					getBrain().setDebugString("Searching food");
					if (getBrain().isBlocked())
						getBrain().setRandomHeading();
					return WarExplorer.ACTION_MOVE;
				}
				break;
			default:
				break;
			}
		}
		
		ArrayList<WarMessage> msgs = getBrain().getMessages();
		for(WarMessage msg : msgs) {
			if (msg.getMessage().equals("Need food")) {
				if(!getBrain().isBagEmpty())
				{
					getBrain().setHeading(msg.getAngle());
					return WarExplorer.ACTION_MOVE;
				}
				else {
					if (getBrain().isBlocked())
						getBrain().setRandomHeading();
					return WarExplorer.ACTION_MOVE;
				}
			}
			if(msg.getMessage().equals("Don't need food anymore")) {
				if (getBrain().isBlocked())
					getBrain().setRandomHeading();
				return WarExplorer.ACTION_MOVE;
			}
		}
		
		if (getBrain().isBlocked())
			getBrain().setRandomHeading();
		return WarExplorer.ACTION_MOVE;
	}

}
