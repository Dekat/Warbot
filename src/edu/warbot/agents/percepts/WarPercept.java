package edu.warbot.agents.percepts;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentType;

public class WarPercept {

    private ControllableWarAgent observer;

	public WarPercept(ControllableWarAgent observer) {
        this.observer = observer;
	}

    protected ControllableWarAgent getObserver() {
        return observer;
    }
}
