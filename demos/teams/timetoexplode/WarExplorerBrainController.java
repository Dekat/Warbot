package teams.timetoexplode;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.warbot.communications.WarMessage;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private int _idBase;
	private boolean _baseFullLife;
	private Double _positionBase;
	
	public WarExplorerBrainController() {
		super();
		
		_idBase = 0;
		_baseFullLife = false;
		_positionBase = 0.0;
	}

	@Override
	public String action() {
		
		if(_idBase == 0) {
			getBrain().broadcastMessageToAll("Give me your ID base", "");
		} 
		else {
			getBrain().sendMessage(_idBase, "Are you full life", "");
		}
		
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
				if(!getBrain().isEnemy(p)) {
					if(getBrain().isBagEmpty()) {
						getBrain().setRandomHeading();
						return WarExplorer.ACTION_MOVE;
					}
					if(!getBrain().isBagEmpty()){
						if(p.getDistance() < WarBase.MAX_DISTANCE_GIVE) {
							getBrain().setIdNextAgentToGive(p.getID());
							return WarExplorer.ACTION_GIVE;
						}
						else {
							return WarExplorer.ACTION_MOVE;
						}
					}
				}
				break;
			default:
				break;
			}
		}
		
		if(getBrain().isBagFull()) {
			if(!_baseFullLife) {
				getBrain().setHeading(_positionBase);
				return WarEngineer.ACTION_MOVE;
			}
		}
		
		ArrayList<WarMessage> msgs = getBrain().getMessages();
		for(WarMessage msg : msgs) {
			if(msg.getMessage().equals("I am the base and here is my ID")) {
				String[] content = msg.getContent();
				_idBase = Integer.parseInt(content[0]);
			}
			if(msg.getMessage().equals("I am full life")) {
				_baseFullLife = true;
			}
			if(msg.getMessage().equals("I am not full life")) {
				_positionBase = msg.getAngle();
				_baseFullLife = false;
			}
			if (msg.getMessage().equals("Need food")) {
				if(!getBrain().isBagEmpty())
				{
					getBrain().setHeading(msg.getAngle());
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
