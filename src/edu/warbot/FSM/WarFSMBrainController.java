package edu.warbot.FSM;

import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarExplorerAdapter;

//Ici plutot mettre ControllableWarAgentAdapter ?
public class WarFSMBrainController extends WarBrain<WarExplorerAdapter>{
	
	private WarFSM<ControllableWarAgentAdapter> fsm;
	
	public void setFSM(WarFSM<ControllableWarAgentAdapter> fsm){
		this.fsm = fsm;
	}
	
	@Override
	public String action() {
		return fsm.executeFSM();
	}

	public WarFSM getFSM() {
		return fsm;
	}

}
