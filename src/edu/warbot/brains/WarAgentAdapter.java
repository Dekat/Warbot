package edu.warbot.brains;

import madkit.kernel.AbstractAgent.ReturnCode;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.capacities.CommonCapacities;

public abstract class WarAgentAdapter implements CommonCapacities {
	
	private WarAgent _agent;
	
	public WarAgentAdapter(WarAgent agent) {
		this._agent = agent;
	}
	
	protected WarAgent getAgent() {
		return _agent;
	}
	
	@Override
	public double getHeading() {
		return getAgent().getHeading();
	}
	
	@Override
	public void setHeading(double angle) {
		getAgent().setHeading(angle);
	}
	
	@Override
	public void setRandomHeading() {
		getAgent().setRandomHeading();
	}
	
	@Override
	public void setRandomHeading(int range){
		getAgent().setRandomHeading(range);
	}
	
	@Override
	public String getTeamName() {
		return getAgent().getTeamName();
	}
	
	@Override
	public boolean isEnemy(WarPercept percept) {
		return getAgent().isEnemy(percept);
	}

	@Override
	public int getID() {
		return getAgent().getID();
	}

	@Override
	public ReturnCode requestRole(String group, String role) {
		return getAgent().requestRole(group, role);
	}

	@Override
	public ReturnCode leaveRole(String group, String role) {
		return getAgent().leaveRole(group, role);
	}

	@Override
	public ReturnCode leaveGroup(String group) {
		return getAgent().leaveGroup(group);
	}

	@Override
	public int numberOfAgentsInRole(String group, String role) {
		return getAgent().numberOfAgentsInRole(group, role);
	}
}
