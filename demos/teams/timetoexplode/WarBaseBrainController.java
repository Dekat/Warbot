package teams.timetoexplode;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarKamikaze;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarBaseAdapter;
import edu.warbot.communications.WarMessage;

public class WarBaseBrainController extends WarBrain<WarBaseAdapter> {

	private boolean _inDanger;
	private int _nbKamikaze;
	
	public WarBaseBrainController() {
		super();
		
		_inDanger = false;
		_nbKamikaze = 0;
	}
	
	@Override
	public String action() {
		
		if((getAgent().getHealth() < WarBase.MAX_HEALTH) && (!getAgent().isBagEmpty())) {
			return WarBase.ACTION_EAT;
		}
		
		if((getAgent().getHealth() > WarKamikaze.COST) && (_nbKamikaze < ((WarBase.MAX_HEALTH / WarKamikaze.COST) - 1))) {
			getAgent().setNextAgentToCreate(WarAgentType.WarKamikaze);
			_nbKamikaze++;
			return WarBase.ACTION_CREATE;
		}
		
		ArrayList<WarMessage> msgs = getAgent().getMessages();
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("Give me your ID base")) {
				getAgent().reply(msg, "I am the base and here is my ID", Integer.toString(getAgent().getID()));
			}
			if(msg.getMessage().equals("Are you full life")) {
				if(getAgent().getHealth() < WarBase.MAX_HEALTH) {
					getAgent().reply(msg, "I am not full life", "");
				}
				else {
					getAgent().reply(msg, "I am full life", "");
				}
			}
		}
		
		ArrayList<WarPercept> percepts = getAgent().getPercepts();
		
		for (WarPercept p : percepts) {
			switch(p.getType()) {
			case WarRocketLauncher :
				if(getAgent().isEnemy(p)) {
					if(getAgent().getHealth() < (WarBase.MAX_HEALTH - WarEngineer.COST)) {
						getAgent().broadcastMessageToAll("We are under attack", String.valueOf(p.getAngle()), String.valueOf(p.getDistance()));
						_inDanger = true;
					}
				}
				break;
			case WarKamikaze :
				if(getAgent().isEnemy(p)) {
					if(getAgent().getHealth() < (WarBase.MAX_HEALTH - WarEngineer.COST)) {
						getAgent().broadcastMessageToAll("We are under attack", String.valueOf(p.getAngle()), String.valueOf(p.getDistance()));
						_inDanger = true;
					}
				}
				break;
			default:
				break;
			}
		}
		
		if(_inDanger) {
			ArrayList<WarPercept> enemies = getAgent().getPerceptsEnemies();
			if(enemies.isEmpty()) {
				_inDanger = false;
				getAgent().broadcastMessageToAll("I am the danger", "");
			}
		}
		
		return WarBase.ACTION_IDLE;
	}

}
