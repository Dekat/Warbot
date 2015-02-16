package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.FSMEditor.settings.EnumMessage;
import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;
import edu.warbot.tools.geometry.CoordPolar;

public class WarActionSendMessage extends WarAction<WarRocketLauncherAdapter> {
	
	EnumMessage message;
	WarAgentType agentType;
	
	public WarActionSendMessage(WarRocketLauncherAdapter agentAdapter, WarAgentType agentType, EnumMessage message) {
		super(agentAdapter);
		this.agentType = agentType;
		this.message = message;
	}

	public String executeAction(){
		
		getAgent().broadcastMessageToAgentType(this.agentType, message.name(), "");
		
		return MovableWarAgent.ACTION_MOVE;
		
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
	
}
