package edu.warbot.agents;

import edu.warbot.agents.actions.ControllableActions;
import edu.warbot.game.Team;

import java.awt.*;

public class WarBuilding extends WarAgent implements ControllableActions {

	public WarBuilding(Team team, Shape hitbox) {
		super(ACTION_IDLE, team, hitbox);
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
