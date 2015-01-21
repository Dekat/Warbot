package teams.timetoexplode;

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
	
	public WarEngineerBrainController() {
		super();
	}

	@Override
	public String action() {
		
		return WarEngineer.ACTION_IDLE;
	}
}
