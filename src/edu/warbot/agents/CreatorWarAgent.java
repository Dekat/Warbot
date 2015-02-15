package edu.warbot.agents;

import edu.warbot.agents.actions.CreatorActions;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.CreatorWarAgentAdapter;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.capacities.Creator;
import edu.warbot.game.Team;

import java.awt.*;

public abstract class CreatorWarAgent extends ControllableWarAgent implements CreatorActions, Creator {

	private WarAgentType _nextAgentToCreate;

	public CreatorWarAgent(String firstActionToDo, Team team, Hitbox hitbox, WarBrain<? extends CreatorWarAgentAdapter> brainController, double distanceOfView, double angleOfView, int cost,	int maxHealth, int bagSize) {
		super(firstActionToDo, team, hitbox, brainController, distanceOfView, angleOfView, cost, maxHealth, bagSize);

		_nextAgentToCreate = WarAgentType.WarExplorer;
	}

	@Override
	public String create() {
		getTeam().createUnit(this, _nextAgentToCreate);
		return getBrain().action();
	}

	@Override
	public void setNextAgentToCreate(WarAgentType nextAgentToCreate) {
		_nextAgentToCreate = nextAgentToCreate;
	}

	@Override
	public WarAgentType getNextAgentToCreate() {
		return _nextAgentToCreate;
	}

}
