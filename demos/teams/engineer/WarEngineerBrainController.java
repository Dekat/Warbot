package teams.engineer;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.agents.WarTurret;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.braincontrollers.WarEngineerAbstractBrainController;
import edu.warbot.communications.WarMessage;

public class WarEngineerBrainController extends WarEngineerAbstractBrainController {
	
	private int _idBase;
	private boolean _aim;
	private boolean _defenseOnDuty;
	private Double _angleDirection;
	private int _state; //0 : normal, 1 : contournement, 2 : faim
	private int _distance;
	private Double _randomHeading;
	private int _nbTurrets;

	
	public WarEngineerBrainController() {
		super();
		
		_idBase = 0;
		_aim = false;
		_defenseOnDuty = false;
		_angleDirection = 0.0;
		_state = 0;
		_distance = 0;
		_randomHeading = 0.0;
		_nbTurrets = 0;

	}

	@Override
	public String action() {
		
		if(_idBase == 0) {
			getBrain().broadcastMessageToAll("Give me your ID base", "");
		}
		
		if(getBrain().getHealth() < WarEngineer.MAX_HEALTH && !getBrain().isBagEmpty()) {
			return WarEngineer.ACTION_EAT;
		}
		
		ArrayList<WarMessage> msgs = getBrain().getMessages();
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("I am the base and here is my ID")) {
				String[] content = msg.getContent();
				_idBase = Integer.parseInt(content[0]);
			}
			if(msg.getMessage().equals("Here is your next target")) {
				String[] content = msg.getContent();
				_angleDirection = Double.parseDouble(content[0]);
				getBrain().setHeading(_angleDirection);
				return WarEngineer.ACTION_MOVE;
			}
			if(msg.getMessage().equals("You are on aim")) {
				//_aim = true;
				if(getBrain().getHealth() > WarTurret.COST) {
					getBrain().setDebugString("Creating tower");
					getBrain().broadcastMessageToAll("Don't need food anymore", "");
					getBrain().setNextAgentToCreate(WarAgentType.WarTurret);
					_angleDirection = 0.0;
					_nbTurrets++;
					if(_nbTurrets == 4) {
						_aim = true;
					}
					return WarEngineer.ACTION_CREATE;
				}
				else {
					_state = 2;
				}
			}
			if(msg.getMessage().equals("You are not on aim")) {
				getBrain().setDebugString("I don't stop");
				return WarEngineer.ACTION_MOVE;
			}
		}
		
		if(_state == 0) {
			
			if(!_defenseOnDuty) {
				if(!_aim) {
					if(_angleDirection == 0.0) {
						getBrain().sendMessage(_idBase, "Give me my next target", "");
						getBrain().setDebugString("Give me my next target");
						//System.out.println("Give me my next target");
						return WarEngineer.ACTION_IDLE;
					}
					else {
						if(getBrain().isBlocked()) {
							_state = 1;
							return WarEngineer.ACTION_IDLE;
						}
						getBrain().setDebugString("Going to put a turret");
						getBrain().sendMessage(_idBase, "Am I on aim", "");
						//System.out.println("I ask if I'm on aim");
						//return WarEngineer.ACTION_MOVE;
					}
				}
			}
			
		}
		if(_state == 1) {
			//getBrain().setHeading(_angleDirection + 120);
			if(_randomHeading == 0.0) {
				getBrain().setRandomHeading();
				_randomHeading = getBrain().getHeading();
			}
			if(_distance < 20) {
				_distance++;
				return WarEngineer.ACTION_MOVE;
			}
			else {
				_randomHeading = 0.0;
				_angleDirection = 0.0;
				_distance = 0;
				_state = 0;
				return WarEngineer.ACTION_IDLE;
			}
		}
		if(_state == 2) {
			if(getBrain().getHealth() <= WarTurret.COST) {
				getBrain().broadcastMessageToAll("Need food", "");
				return WarEngineer.ACTION_IDLE;
			}
			else {
				getBrain().setDebugString("Creating tower");
				getBrain().broadcastMessageToAll("Don't need food anymore", "");
				getBrain().setNextAgentToCreate(WarAgentType.WarTurret);
				_angleDirection = 0.0;
				_nbTurrets++;
				if(_nbTurrets == 4) {
					_aim = true;
				}
				_state = 0;
				return WarEngineer.ACTION_CREATE;
			}
		}
		
		return WarEngineer.ACTION_IDLE;
	}
}
