package teams.timetoexplode;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.warbot.communications.WarMessage;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {
	
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
			getBrain().broadcastMessageToAll("Your ID please", "");
		}
		
		if (getBrain().getHealth() <= (WarRocketLauncher.MAX_HEALTH / 5))
			return WarRocketLauncher.ACTION_EAT;
		
		ArrayList<WarPercept> percepts = getBrain().getPercepts();	
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			case WarFood :
				if (p.getDistance() < WarFood.MAX_DISTANCE_TAKE && !getBrain().isBagFull()) {
					getBrain().setHeading(p.getAngle());
					return WarExplorer.ACTION_TAKE;
				}else if(!getBrain().isBagFull()){
					getBrain().setHeading(p.getAngle());
				}
				break;
			case WarBase :
				if (getBrain().isEnemy(p)) {
					_baseFound = true;
					getBrain().setHeading(p.getAngle());
					if (getBrain().isReloaded()) {
						return WarRocketLauncher.ACTION_FIRE;
					} else
						return WarRocketLauncher.ACTION_RELOAD;
				}
				break;
			case WarRocketLauncher :
				if (getBrain().isEnemy(p)) {
					getBrain().setHeading(p.getAngle());
					if (getBrain().isReloaded()) {
						return WarRocketLauncher.ACTION_FIRE;
					} else
						return WarRocketLauncher.ACTION_RELOAD;
				}
				break;
			default:
				break;
			}
		}
		
		ArrayList<WarMessage> msgs = getBrain().getMessages();
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("Enemy base on sight") && !_inDanger) {
				getBrain().setHeading(msg.getAngle());
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
				getBrain().setRandomHeading();
			}
		}
		
		if(_inDanger && !_baseFound) {
			getBrain().setHeading(_basePosition);
		}
		
		if (getBrain().isBlocked())
			getBrain().setRandomHeading();
		return WarRocketLauncher.ACTION_MOVE;
	}
	
}