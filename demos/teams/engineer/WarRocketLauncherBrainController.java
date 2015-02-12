package teams.engineer;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;
import edu.warbot.communications.WarMessage;

public class WarRocketLauncherBrainController extends WarBrain<WarRocketLauncherAdapter> {
	
	private boolean _baseFound;
	private boolean _inDanger;
	private int _baseId;
	private Double _basePosition;

	public WarRocketLauncherBrainController() {
		super();
		
		_baseFound = false;
		_inDanger = false;
		_baseId = 0;
		_basePosition = 0.0;
	}
	
	@Override
	public String action() {
		
		if(_baseId == 0) {
			getAgent().broadcastMessageToAll("Your ID please", "");
		}
		
		if (getAgent().getHealth() <= (WarRocketLauncher.MAX_HEALTH / 5))
			return WarRocketLauncher.ACTION_EAT;
		
		ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();
		for (WarAgentPercept p : percepts) {
			switch(p.getType()) {
			case WarFood :
				if (p.getDistance() < WarFood.MAX_DISTANCE_TAKE && !getAgent().isBagFull()) {
					getAgent().setHeading(p.getAngle());
					return WarExplorer.ACTION_TAKE;
				}else if(!getAgent().isBagFull()){
					getAgent().setHeading(p.getAngle());
				}
				break;
			case WarBase :
				if (getAgent().isEnemy(p)) {
					_baseFound = true;
					getAgent().setHeading(p.getAngle());
					if (getAgent().isReloaded()) {
						return WarRocketLauncher.ACTION_FIRE;
					} else
						return WarRocketLauncher.ACTION_RELOAD;
				}
				break;
			case WarRocketLauncher :
				if (getAgent().isEnemy(p)) {
					getAgent().setHeading(p.getAngle());
					if (getAgent().isReloaded()) {
						return WarRocketLauncher.ACTION_FIRE;
					} else
						return WarRocketLauncher.ACTION_RELOAD;
				}
				break;
			default:
				break;
			}
		}
		
		ArrayList<WarMessage> msgs = getAgent().getMessages();
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("Enemy base on sight") && !_inDanger) {
				getAgent().setHeading(msg.getAngle());
			}
			if(msg.getMessage().equals("We are under attack")) {
				_inDanger = true;
				_basePosition = msg.getAngle();
			}
			if(msg.getMessage().equals("Here is my ID")) {
				String[] content = msg.getContent();
				_baseId = Integer.parseInt(content[0]);
			}
			if(msg.getMessage().equals("I am the danger")) {
				_inDanger = false;
				getAgent().setRandomHeading();
			}
		}
		
		if(_inDanger && !_baseFound) {
			getAgent().setHeading(_basePosition);
		}
		
		if (getAgent().isBlocked())
			getAgent().setRandomHeading();
		return WarRocketLauncher.ACTION_MOVE;
	}
	
}