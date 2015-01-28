package edu.warbot.agents;

import edu.warbot.agents.actions.ControllableActions;
import edu.warbot.game.Team;

@SuppressWarnings("serial")
public class WarBuilding extends WarAgent implements ControllableActions {

	public WarBuilding(Team team, double hitboxRadius) {
		super(ACTION_IDLE, team, hitboxRadius);
	}

	@Override
	public String give() {
		return ACTION_IDLE;
	}

	@Override
	public String eat() {
		return ACTION_IDLE;
	}

	@Override
	public String idle() {
		return ACTION_IDLE;
	}

}
