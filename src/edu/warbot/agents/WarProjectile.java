package edu.warbot.agents;

import java.util.List;
import java.util.logging.Level;

import edu.warbot.agents.capacities.Movable;
import edu.warbot.game.Game;
import edu.warbot.game.Team;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.CoordPolar;
import edu.warbot.tools.WarMathTools;

@SuppressWarnings("serial")
public abstract class WarProjectile extends WarAgent implements Movable {

	private double _speed;
	private double _explosionRadius;
	private int _damage;
	private int _autonomy;
	private WarAgent _sender;
	private int _currentAutonomy;

	public WarProjectile(String firstActionToDo, Team team, double hitboxRadius, WarAgent sender, double speed, double explosionRadius, int damage, int autonomy) {
		super(firstActionToDo, team, hitboxRadius);

		this._speed = speed;
		this._explosionRadius = explosionRadius;
		this._damage = damage;
		this._autonomy = autonomy;
		this._currentAutonomy = autonomy;
		this._sender = sender;
	}

	@Override
	protected void activate() {
		super.activate();
		setHeading(_sender.getHeading());
		setXY(_sender.getX(), _sender.getY());
	}

	@Override
	protected void doOnEachTick() {
		super.doOnEachTick();
		_currentAutonomy--;
		if (_currentAutonomy < 0)
			explode();
	}

	@Override
	public String move() {
		logger.log(Level.FINEST, this.toString() + " moving...");
		if (! isBlocked()) {
			fd(getSpeed());
		} else
			explode();
		return ACTION_MOVE;
	}

	private void explode() {
		if (isAlive()) {
			killAgent(this);
			// On va infliger des d�g�ts � tous les agents dans le radius de l'explosion
			List<WarAgent> touchedAgents = Game.getInstance().getAllAgentsInRadiusOf(this, _explosionRadius);
			for (WarAgent agent : touchedAgents) {
				if (agent instanceof ControllableWarAgent) {
					((ControllableWarAgent) agent).damage(_damage);
				} else if (agent instanceof WarProjectile) {
					((WarProjectile) agent).explode();
				}
			}
		}
	}

	protected boolean isGoingToCrossAnOtherAgent() {
		for(WarAgent a : Game.getInstance().getAllAgentsInRadiusOf(this, getHitboxRadius() + getSpeed())) {
			if (a.getID() != _sender.getID() && a.getID() != getID()) {
				double currentStep = 0;
				while(currentStep < getSpeed()) {
					if (isInCollisionWithAtPosition(
							WarMathTools.addTwoPoints(new CoordCartesian(getX(), getY()), new CoordPolar(currentStep, getHeading())),
							a)) {
						return true;
					}
					currentStep += 1.0;
				}
				return isInCollisionWithAtPosition(
						WarMathTools.addTwoPoints(new CoordCartesian(getX(), getY()), new CoordPolar(getSpeed(), getHeading())),
						a);
			}
		}
		return false;
	}
	
	@Override
	public boolean isBlocked() {
		return isGoingToBeOutOfMap() || isGoingToCrossAnOtherAgent();
	}

	public double getSpeed() {
		return _speed;
	}

	public double getExplosionRadius() {
		return _explosionRadius;
	}

	public int getDamage() {
		return _damage;
	}

	public int getAutonomy() {
		return _autonomy;
	}
	
	public int getCurrentAutonomy() {
		return _currentAutonomy;
	}
}
