package edu.warbot.agents.percepts;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentType;

public class WarPercept implements Comparable<WarPercept>{

	private double _angle;
	private double _distance;
	private int	_id;
	private String _teamName;
	private WarAgentType _type;
	private double _heading;
	private int _health;

	public WarPercept(ControllableWarAgent observer, WarAgent seenAgent) {
		_angle = observer.getPosition().getAngleToPoint(seenAgent.getPosition());
		this._distance = observer.getDistanceFrom(seenAgent);
		this._id = seenAgent.getID();
		this._teamName = seenAgent.getTeam().getName();
		this._type = WarAgentType.valueOf(seenAgent.getClass().getSimpleName());
		this._heading = seenAgent.getHeading();
		
		if (seenAgent instanceof ControllableWarAgent) {
			_health = ((ControllableWarAgent) seenAgent).getHealth();
		} else {
			_health = 0;
		}
	}
	
	public double getAngle() {
		return _angle;
	}

	public double getDistance() {
		return _distance;
	}

	public int getID() {
		return _id;
	}

	public String getTeamName() {
		return _teamName;
	}

	public WarAgentType getType() {
		return _type;
	}

	public double getHeading() {
		return _heading;
	}

	public int getHealth() {
		return _health;
	}
	
	@Override
	public String toString(){
		return "Percept : " + _type + " " + _id + " : team = " + _teamName + " : " + _angle + "° " + _distance;
	}

	@Override
	public int compareTo(WarPercept percept) {
		if (getDistance() > percept.getDistance())
			return 1;
		else if (getDistance() < percept.getDistance())
			return -1;
		return 0;
	}
}
