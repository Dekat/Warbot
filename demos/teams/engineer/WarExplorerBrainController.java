package teams.engineer;

import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarExplorerAdapter;
import edu.warbot.communications.WarMessage;

import java.util.ArrayList;

public class WarExplorerBrainController extends WarBrain<WarExplorerAdapter> {

	private boolean _starving;
	
	public WarExplorerBrainController() {
		super();
		
		_starving = false;
	}

	@Override
	public String action() {
		ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();
		
		for (WarAgentPercept p : percepts) {
			switch(p.getType()) {
			case WarFood :
				if(p.getDistance() < WarFood.MAX_DISTANCE_TAKE && !getAgent().isBagFull()) {
					getAgent().setHeading(p.getAngle());
					return WarExplorer.ACTION_TAKE;
				}else if(!getAgent().isBagFull()){
					getAgent().setHeading(p.getAngle());
				}
				break;
			case WarBase :
				if(getAgent().isEnemy(p)) {
					getAgent().broadcastMessageToAll("Enemy base on sight", String.valueOf(p.getAngle()), String.valueOf(p.getDistance()));
				}
				break;
			case WarEngineer :
				if(p.getDistance() < WarEngineer.MAX_DISTANCE_GIVE && getAgent().getNbElementsInBag() > 0) {
					getAgent().setDebugString("Giving food");
					getAgent().setIdNextAgentToGive(p.getID());
					return WarExplorer.ACTION_GIVE;
				}
				if(getAgent().isBagEmpty()) {
					getAgent().setDebugString("Searching food");
					if (getAgent().isBlocked())
						getAgent().setRandomHeading();
					return WarExplorer.ACTION_MOVE;
				}
				break;
			default:
				break;
			}
		}
		
		ArrayList<WarMessage> msgs = getAgent().getMessages();
		for(WarMessage msg : msgs) {
			if (msg.getMessage().equals("Need food")) {
				if(!getAgent().isBagEmpty())
				{
					getAgent().setHeading(msg.getAngle());
					return WarExplorer.ACTION_MOVE;
				}
				else {
					if (getAgent().isBlocked())
						getAgent().setRandomHeading();
					return WarExplorer.ACTION_MOVE;
				}
			}
			if(msg.getMessage().equals("Don't need food anymore")) {
				if (getAgent().isBlocked())
					getAgent().setRandomHeading();
				return WarExplorer.ACTION_MOVE;
			}
		}
		
		if (getAgent().isBlocked())
			getAgent().setRandomHeading();
		return WarExplorer.ACTION_MOVE;
	}

}
