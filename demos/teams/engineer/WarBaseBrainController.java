package teams.engineer;

import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarBaseAdapter;
import edu.warbot.communications.WarMessage;

import java.util.ArrayList;

public class WarBaseBrainController extends WarBrain<WarBaseAdapter> {

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
			getAgent().setNextAgentToCreate(WarAgentType.WarEngineer);
			_alreadyCreated = true;
			return WarBase.ACTION_CREATE;
		}
		
		ArrayList<WarMessage> msgs = getAgent().getMessages();
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("Give me your ID base")) {
				getAgent().reply(msg, "I am the base and here is my ID", Integer.toString(getAgent().getID()));
			}
			if(msg.getMessage().equals("Give me my next target")) {
				
				
				ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();
				
				for (WarAgentPercept p : percepts) {
					switch(p.getType()) {
					case WarEngineer :
						if(!getAgent().isEnemy(p)) {
							Double angleAim = getAngleRT(getXRT(p.getDistance(),p.getAngle()),getYRT(p.getDistance(),p.getAngle())); 
							getAgent().reply(msg, "Here is your next target", Double.toString(angleAim));
						}
						break;
					default:
						break;
					}
				}
			}
			if(msg.getMessage().equals("Am I on aim")) {
				ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();
				
				for (WarAgentPercept p : percepts) {
					switch(p.getType()) {
					case WarEngineer :
						if(!getAgent().isEnemy(p)) {
							if(p.getAngle() >= ((_towerCount*45)-5) && p.getAngle() <= ((_towerCount*45)+5) && p.getDistance() >= 38 && p.getDistance() <= 42)
							{
								_towerCount += 2;
								getAgent().reply(msg, "You are on aim", "");
							}
							else {
								getAgent().reply(msg, "You are not on aim", "");
							}
						}
						break;
					default:
						break;
					}
				}
			}
			
		}
		
		ArrayList<WarAgentPercept> percepts = getAgent().getPercepts();
		
		//System.out.println(percepts);
		
		for (WarAgentPercept p : percepts) {
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
			ArrayList<WarAgentPercept> enemies = getAgent().getPerceptsEnemies();
			if(enemies.isEmpty()) {
				_inDanger = false;
				getAgent().broadcastMessageToAll("I am the danger", "");
			}
		}
		
		return WarBase.ACTION_IDLE;
	}

}
