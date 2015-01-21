package teams.timetoexplode;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarKamikaze;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.warbot.communications.WarMessage;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	private boolean _inDanger;
	private int _nbKamikaze;
	
	public WarBaseBrainController() {
		super();
		
		_inDanger = false;
		_nbKamikaze = 0;
	}
	
	@Override
	public String action() {
		
		if((getBrain().getHealth() < WarBase.MAX_HEALTH) && (!getBrain().isBagEmpty())) {
			return WarBase.ACTION_EAT;
		}
		
		if((getBrain().getHealth() > WarKamikaze.COST) && (_nbKamikaze < ((WarBase.MAX_HEALTH / WarKamikaze.COST) - 1))) {
			getBrain().setNextAgentToCreate(WarAgentType.WarKamikaze);
			_nbKamikaze++;
			return WarBase.ACTION_CREATE;
		}
		
		ArrayList<WarMessage> msgs = getBrain().getMessages();
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("Give me your ID base")) {
				getBrain().reply(msg, "I am the base and here is my ID", Integer.toString(getBrain().getID()));
			}
			if(msg.getMessage().equals("Are you full life")) {
				if(getBrain().getHealth() < WarBase.MAX_HEALTH) {
					getBrain().reply(msg, "I am not full life", "");
				}
				else {
					getBrain().reply(msg, "I am full life", "");
				}
			}
		}
		
		ArrayList<WarPercept> percepts = getBrain().getPercepts();
		
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			case WarRocketLauncher :
				if(getBrain().isEnemy(p)) {
					if(getBrain().getHealth() < (WarBase.MAX_HEALTH - WarEngineer.COST)) {
						getBrain().broadcastMessageToAll("We are under attack", String.valueOf(p.getAngle()), String.valueOf(p.getDistance()));
						_inDanger = true;
					}
				}
				break;
			case WarKamikaze :
				if(getBrain().isEnemy(p)) {
					if(getBrain().getHealth() < (WarBase.MAX_HEALTH - WarEngineer.COST)) {
						getBrain().broadcastMessageToAll("We are under attack", String.valueOf(p.getAngle()), String.valueOf(p.getDistance()));
						_inDanger = true;
					}
				}
				break;
			default:
				break;
			}
		}
		
		if(_inDanger) {
			ArrayList<WarPercept> enemies = getBrain().getPerceptsEnemies();
			if(enemies.isEmpty()) {
				_inDanger = false;
				getBrain().broadcastMessageToAll("I am the danger", "");
			}
		}
		
		return WarBase.ACTION_IDLE;
	}

}
