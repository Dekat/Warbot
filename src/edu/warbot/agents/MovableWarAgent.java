package edu.warbot.agents;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;

import edu.warbot.agents.actions.MovableActions;
import edu.warbot.agents.actions.PickerActions;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.capacities.Movable;
import edu.warbot.brains.capacities.Picker;
import edu.warbot.game.Team;

public abstract class MovableWarAgent extends ControllableWarAgent implements PickerActions, Picker, MovableActions, Movable {

	private double _speed;

	public MovableWarAgent(String firstActionToDo, Team team, Hitbox hitbox, WarBrain<? extends MovableWarAgentAdapter> brainController, double distanceOfView, double angleOfView, int cost, int maxHealth, int bagSize, double speed) {
		super(firstActionToDo, team, hitbox, brainController, distanceOfView, angleOfView, cost, maxHealth, bagSize);

		this._speed = speed;
	}

	public double getSpeed() {
		return _speed;
	}

	@Override
	public String move() {
		logger.log(Level.FINEST, this.toString() + " moving...");
		if (!isBlocked()) {
			logger.log(Level.FINER, this.toString() + " moved of " + getSpeed());
			fd(getSpeed());
		}
		return getBrain().action();
	}

	@Override
	public String take() {
		logger.log(Level.FINEST, this.toString() + " taking...");
		if (! isBagFull()) {
			ArrayList<WarResource> reachableResources = getTeam().getGame().getMotherNatureTeam().getAccessibleResourcesFor(this);
			if (reachableResources.size() > 0) {
//				killAgent(reachableResources.get(0));
                reachableResources.get(0).kill();
				addElementInBag();
				logger.log(Level.FINER, this.toString() + " take " + reachableResources.get(0).getClass().getSimpleName());
			}
		}
		return getBrain().action();
	}

	@Override
	public boolean isBlocked() {
		return isGoingToBeOutOfMap() || isGoingToBeOverAnOtherAgent();
	}
}
