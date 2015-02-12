package edu.warbot.FSM.action;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Reste sans bougé
 * @author Olivier
 *
 */
public class WarActionIdle<AgentAdapterType extends ControllableWarAgentAdapter> extends WarAction<AgentAdapterType> {

	private int nombrePas;
	private int currentPas = 0;
	
	public WarActionIdle(AgentAdapterType brain, int nombrePas) {
		super(brain);
		this.nombrePas = nombrePas;
	}
	
	public String executeAction(){
		
		return MovableWarAgent.ACTION_IDLE;
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
		currentPas = 0;
	}

}
