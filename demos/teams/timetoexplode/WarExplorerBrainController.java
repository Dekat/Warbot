package teams.timetoexplode;

import java.util.ArrayList;

import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarExplorerAdapter;
import edu.warbot.communications.WarMessage;

public class WarExplorerBrainController extends WarBrain<WarExplorerAdapter> {

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
			getAgent().broadcastMessageToAll("Give me your ID base", "");
		} 
		else {
			getAgent().sendMessage(_idBase, "Are you full life", "");
		}
		
		ArrayList<WarPercept> percepts = getAgent().getPercepts();
		
		for (WarPercept p : percepts) {
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
				if(!getAgent().isEnemy(p)) {
					if(getAgent().isBagEmpty()) {
						getAgent().setRandomHeading();
						return WarExplorer.ACTION_MOVE;
					}
					if(!getAgent().isBagEmpty()){
						if(p.getDistance() < WarBase.MAX_DISTANCE_GIVE) {
							getAgent().setIdNextAgentToGive(p.getID());
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
		
		if(getAgent().isBagFull()) {
			if(!_baseFullLife) {
				getAgent().setHeading(_positionBase);
				return WarEngineer.ACTION_MOVE;
			}
		}
		
		ArrayList<WarMessage> msgs = getAgent().getMessages();
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
				if(!getAgent().isBagEmpty())
				{
					getAgent().setHeading(msg.getAngle());
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
