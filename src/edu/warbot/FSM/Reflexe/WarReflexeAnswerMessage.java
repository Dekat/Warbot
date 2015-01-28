package edu.warbot.FSM.Reflexe;

import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.communications.WarMessage;

public class WarReflexeAnswerMessage<AgentAdapterType extends ControllableWarAgentAdapter> extends WarReflexe<AgentAdapterType>{

	public WarReflexeAnswerMessage(AgentAdapterType brain, String message, String answer) {
		super(brain, "Reflexe answer message");
	}

	@Override
	public String executeReflexe() {
		for (WarMessage m : getBrain().getMessages()) {
			if(m.getMessage().equals("whereAreYou")){
				getBrain().sendMessage(m.getSenderID(), "here", "");
			}
		}
		return null;
	}

}
