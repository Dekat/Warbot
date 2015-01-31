package edu.warbot.agents;

import java.awt.Dimension;

import madkit.kernel.AbstractAgent;
import turtlekit.kernel.Turtle;

import com.badlogic.gdx.math.Circle;

import edu.warbot.agents.actions.MovableActions;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.capacities.CommonCapacities;
import edu.warbot.brains.capacities.Movable;
import edu.warbot.game.Team;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.CoordPolar;
import edu.warbot.tools.WarMathTools;

public abstract class WarAgent extends Turtle implements CommonCapacities {

	private double _hitboxRadius;
	private Team _team;
	private int _dyingStep;

	public WarAgent(String firstActionToDo, Team team, double hitboxRadius) {
		super(firstActionToDo);
		this._team = team;
		this._hitboxRadius = hitboxRadius;
		_dyingStep = 0;
	}

	@Override
	protected void activate() {
		super.activate();
		getTeam().addWarAgent(this);
		requestRole(Team.DEFAULT_GROUP_NAME, getClass().getSimpleName());
	}

	@Override
	protected void end() {
		super.end();
		if (this instanceof WarResource)
			getTeam().removeWarAgent(this);
		else
			getTeam().setWarAgentAsDying(this);
	}

	protected void doOnEachTick() { }

	@Override
	public AbstractAgent.ReturnCode requestRole(String group, String role) {
		createGroupIfAbsent(getTeam().getName(), group);
		return requestRole(getTeam().getName(), group, role);
	}
	
	@Override
	public AbstractAgent.ReturnCode leaveRole(String group, String role) {
		return super.leaveRole(getTeam().getName(), group, role);
	}

	@Override
	public AbstractAgent.ReturnCode leaveGroup(String group) {
		return super.leaveGroup(getTeam().getName(), group);
	}

	@Override
	public int numberOfAgentsInRole(String group, String role) {
		return (getAgentsWithRole(getTeam().getName(), group, role).size());
	}

	public Team getTeam() {
		return _team;
	}
	
	@Override
	public String getTeamName() {
		return getTeam().getName();
	}
	
	@Override
	public boolean isEnemy(WarPercept percept) {
		return ! percept.getTeamName().equals(getTeamName());
	}

	public double getHitboxRadius() {
		return _hitboxRadius;
	}

	public String toString() {
		return "[" + getID() + "] " + getClass().getSimpleName() + " (" + getTeam().getName() + ")";
	}
	
	@Override
	public void setRandomHeading() {
		randomHeading();
	}
	
	@Override
	public void setRandomHeading(int range) {
		randomHeading(range);
	}

	protected boolean isGoingToBeOutOfMap() {
		// Si c'est un monde ouvert, l'agent ne sera jamais en dehors de la carte
		if (getTeam().getGame().getSettings().isOpenWorld())
			return false;
		
		CoordCartesian nextPos = new CoordCartesian(getX(), getY());
		if (this instanceof MovableActions)
			nextPos.add(new CoordPolar(((Movable) this).getSpeed(), getHeading()).toCartesian());

		Dimension mapSize = getTeam().getGame().getMap().getSize();
		return ((nextPos.getX() - getHitboxRadius()) < 0) || ((nextPos.getX() + getHitboxRadius()) > mapSize.width) ||
				((nextPos.getY() - getHitboxRadius()) < 0) || ((nextPos.getY() + getHitboxRadius()) > mapSize.height);
	}

	protected boolean isGoingToBeOnAnOtherAgent() {
		CoordCartesian futurePosition = getPosition();
		double radius = getHitboxRadius();
		if (this instanceof MovableActions) {
			radius += ((Movable) this).getSpeed();
			futurePosition = WarMathTools.addTwoPoints(new CoordCartesian(getX(), getY()), new CoordPolar(((Movable) this).getSpeed(), getHeading()));
		}
		for(WarAgent a : getTeam().getGame().getAllAgentsInRadius(futurePosition.getX(), futurePosition.getY(), radius)) {
			if (a.getID() != getID() && a instanceof ControllableWarAgent) {
				if (this instanceof MovableActions) {
					return isInCollisionWithAtPosition(futurePosition, a);
				} else
					return isInCollisionWith(a);
			}
		}
		return false;
	}

	protected boolean isInCollisionWith(WarAgent agent) {
		return getDistanceFrom(agent) < 0;
	}

	protected boolean isInCollisionWithAtPosition(CoordCartesian pos, WarAgent agent) {
		return WarMathTools.getDistanceBetweenTwoPoints(pos.getX(), pos.getY(), agent.getX(), agent.getY()) < (getHitboxRadius() + agent.getHitboxRadius());
	}
	
	public void setPositionAroundOtherAgent(WarAgent agent) {
		CoordCartesian newPos;
		CoordCartesian agentPos = new CoordCartesian(agent.getX(), agent.getY());
		do {
			randomHeading();
			newPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(getHitboxRadius() + agent.getHitboxRadius(), getHeading()).toCartesian());
			setPosition(newPos);
		} while(isGoingToBeOnAnOtherAgent() || isGoingToBeOutOfMap());
	}
	
	public void moveOutOfCollisionZone() {
		// Test of Collision with map
		Dimension mapSize = getTeam().getGame().getMap().getSize();
		if ((getX() - getHitboxRadius()) < 0)
			setPosition(getHitboxRadius() + 1, getY());
		else if ((getX() + getHitboxRadius()) > mapSize.width)
			setPosition(mapSize.width - getHitboxRadius() - 1, getY());
		if ((getY() - getHitboxRadius()) < 0)
			setPosition(getX(), getHitboxRadius() + 1);
		else if ((getY() + getHitboxRadius()) > mapSize.height)
			setPosition(getX(), mapSize.height - getHitboxRadius() - 1);
		
		for(WarAgent a : getTeam().getGame().getAllAgentsInRadiusOf(this, getHitboxRadius())) {
			if (a.getID() != getID() && a instanceof ControllableWarAgent) {
				if (isInCollisionWith(a)) {
					double angle = getPosition().getAngleToPoint(a.getPosition());
					angle = (angle + 180.) % 360.;
					CoordCartesian newPos = WarMathTools.addTwoPoints(new CoordCartesian(getX(), getY()), 
							new CoordPolar(getHitboxRadius() + a.getHitboxRadius(), angle));
					setPosition(newPos);
				}
			}
		}
	}
	
	public void setRandomPositionInCircle(CoordCartesian centerPoint, double radius) {
		setPosition(WarMathTools.addTwoPoints(centerPoint, CoordPolar.getRandomInBounds(radius).toCartesian()));
	}
	
	public void setRandomPositionInCircle(Circle circle) {
		setRandomPositionInCircle(new CoordCartesian(circle.x, circle.y), circle.radius);
	}
	
	public CoordCartesian getPosition() {
		return new CoordCartesian(getX(), getY());
	}
	
	public void setPosition(double a, double b) {
		super.setXY(a, b);
		moveOutOfCollisionZone();
	}
	
	public void setPosition(CoordCartesian pos) {
		setPosition(pos.getX(), pos.getY());
	}
	
	/**
	 * @return Le nombre de tick passés depuis la mort de l'agent
	 */
	public int getDyingStep() {
		return _dyingStep;
	}
	
	public void incrementDyingStep() {
		_dyingStep++;
	}
	
	public double getDistanceFrom(WarAgent a) {
		return WarMathTools.getDistanceBetweenTwoPoints(getX(), getY(), a.getX(), a.getY())
				- getHitboxRadius() - a.getHitboxRadius();
	}
}
