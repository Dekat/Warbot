package teams.engineer;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.warbot.communications.WarMessage;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	private boolean _alreadyCreated;
	private boolean _inDanger;
	private int _towerCount;
	
	public WarBaseBrainController() {
		super();
		
		_alreadyCreated = false;
		_inDanger = false;
		_towerCount = 1;
	}
	
	//### FONCTIONS DE CALCUL DE TRAJECTOIRE ###\\

	public Double getXRT(Double engD, Double engA) {
		Double cos = Math.cos(Math.toRadians(_towerCount*45));
		cos = cos * 40;
		Double cos2 = Math.cos(Math.toRadians(engA));
		cos2 = cos2 * engD;
		//return ((Math.cos(_towerCount*45))*40)-((Math.cos(engA))*engD);
		return cos - cos2;
	}
	
	public Double getYRT(Double engD, Double engA) {
		Double sin = Math.sin(Math.toRadians(_towerCount*45));
		sin = sin * 40;
		Double sin2 = Math.sin(Math.toRadians(engA));
		sin2 = sin2 * engD;
		//return ((Math.sin(_towerCount*45))*40)-((Math.sin(engA))*engD);
		return sin - sin2;
	}
	
	public int getDistanceRT(Double XRT, Double YRT) {
		return (int) Math.sqrt(Math.pow(XRT,2) + Math.pow(YRT,2));
	}
	
	public Double getAngleRT(Double XRT, Double YRT) {
		if(XRT < 0) {
			return Math.toDegrees(Math.atan(YRT/XRT)) + 180;
		}
		else {
			return Math.toDegrees(Math.atan(YRT/XRT));
		}
	}
	
	@Override
	public String action() {
		
		if(!_alreadyCreated) {
			getBrain().setNextAgentToCreate(WarAgentType.WarEngineer);
			_alreadyCreated = true;
			return WarBase.ACTION_CREATE;
		}
		
		ArrayList<WarMessage> msgs = getBrain().getMessages();
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("Give me your ID base")) {
				getBrain().reply(msg, "I am the base and here is my ID", Integer.toString(getBrain().getID()));
			}
			if(msg.getMessage().equals("Give me my next target")) {
				
				
				ArrayList<WarPercept> percepts = getBrain().getPercepts();
				
				for (WarPercept p : percepts) {
					switch(p.getType()) {
					case WarEngineer :
						if(!getBrain().isEnemy(p)) {
							Double angleAim = getAngleRT(getXRT(p.getDistance(),p.getAngle()),getYRT(p.getDistance(),p.getAngle())); 
							getBrain().reply(msg, "Here is your next target", Double.toString(angleAim));
						}
						break;
					default:
						break;
					}
				}
			}
			if(msg.getMessage().equals("Am I on aim")) {
				ArrayList<WarPercept> percepts = getBrain().getPercepts();
				
				for (WarPercept p : percepts) {
					switch(p.getType()) {
					case WarEngineer :
						if(!getBrain().isEnemy(p)) {
							if(p.getAngle() >= ((_towerCount*45)-5) && p.getAngle() <= ((_towerCount*45)+5) && p.getDistance() >= 38 && p.getDistance() <= 42)
							{
								_towerCount += 2;
								getBrain().reply(msg, "You are on aim", "");
							}
							else {
								getBrain().reply(msg, "You are not on aim", "");
							}
						}
						break;
					default:
						break;
					}
				}
			}
			
		}
		
		ArrayList<WarPercept> percepts = getBrain().getPercepts();
		
		//System.out.println(percepts);
		
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