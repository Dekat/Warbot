package edu.warbot.brains;

import java.awt.Color;
import java.util.ArrayList;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.communications.WarMessage;
import edu.warbot.game.Game;
import edu.warbot.tools.CoordPolar;
import edu.warbot.tools.WarMathTools;

public abstract class WarBrain {
	
	protected ControllableWarAgent _agent;
	
	public WarBrain(ControllableWarAgent agent) {
		this._agent = agent;
	}
	
    public void sendMessage(int idAgent, String message, String ... content) {
    	_agent.sendMessage(idAgent, message, content);
    }
    
	public void broadcastMessageToAll(String message, String ... content) {
		_agent.broadcastMessage(message, content);
	}
	
    public void broadcastMessageToAgentType(WarAgentType agentType, String message, String ... content) {
    	_agent.broadcastMessage(agentType, message, content);
    }
    
    public void broadcastMessage(String groupName, String roleName, String message, String ... content) {
    	_agent.broadcastMessage(groupName, roleName, message, content);
    }
    
    public void reply(WarMessage warMessage, String message, String ... content) {
		_agent.reply(warMessage, message, content);
	}
    
    public ArrayList<WarMessage> getMessages() {
    	return _agent.getMessages();
    }
	
	public void setIdNextAgentToGive(int idNextAgentToGive) {
		_agent.setIdNextAgentToGive(idNextAgentToGive);
	}
	
	public int getBagSize() {
		return _agent.getBagSize();
	}
	
	public int getNbElementsInBag() {
		return _agent.getNbElementsInBag();
	}
	
	public boolean isBagEmpty() {
		return _agent.getNbElementsInBag() == 0;
	}
	
	public boolean isBagFull() {
		return _agent.getNbElementsInBag() == _agent.getBagSize();
	}

	public double getHeading() {
		return _agent.getHeading();
	}
	
	public void setHeading(double angle) {
		_agent.setHeading(angle);
	}
	
	public void setRandomHeading() {
		_agent.randomHeading();
	}
	
	public void setRandomHeading(int range){
		_agent.randomHeading(range);
	}
	
	public void setViewDirection(double viewDirection) {
		_agent.setViewDirection(viewDirection);
	}

	public String getTeamName() {
		return _agent.getTeam().getName();
	}
	
	public boolean isEnemy(WarPercept percept) {
		return (!percept.getTeamName().equals(getTeamName()) &&
				!percept.getTeamName().equals(Game.getInstance().getMotherNatureTeam().getName()));
	}

	public int getID() {
		return _agent.getID();
	}
	
	public int getHealth() {
		return _agent.getHealth();
	}
	
	/** Percepts **/
	
	public ArrayList<WarPercept> getPerceptsAllies() {
		return _agent.getPercepts(true);
	}
	
	public ArrayList<WarPercept> getPerceptsEnemies() {
		return _agent.getPercepts(false);
	}
	
	public ArrayList<WarPercept> getPerceptsResources() {
		return _agent.getPerceptsRessource();
	}
	
	public ArrayList<WarPercept> getPerceptsAlliesByType(WarAgentType agentType){
		return _agent.getPerceptsOfAgentByType(agentType, true);
	}

	public ArrayList<WarPercept> getPerceptsEnemiesByType(WarAgentType agentType){
		return _agent.getPerceptsOfAgentByType(agentType, false);
	}
	
	public ArrayList<WarPercept> getPercepts() {
		return _agent.getPercepts();
	}
	
	public String getDebugString() {
		return _agent.getDebugString();
	}
	
	public void setDebugString(String debugString) {
		_agent.setDebugString(debugString);
	}
	
	public Color getDebugStringColor() {
		return _agent.getDebugStringColor();
	}

	public void setDebugStringColor(Color color) {
		_agent.setDebugStringColor(color);
	}
	
	public CoordPolar getAveragePositionOfUnitInPercept(WarAgentType agentType, boolean ally) {
		return this._agent.getAveragePositionOfUnitInPercept(agentType, ally);
	}
	
	public CoordPolar getIndirectPositionOfAgentWithMessage(WarMessage message) {
		return this._agent.getIndirectPositionOfAgentWithMessage(message);
	}
	
	public CoordPolar getTargetedAgentPosition(double angleToAlly, double distanceFromAlly, double angleFromAllyToTarget, double distanceBetweenAllyAndTarget) {
		return WarMathTools.addTwoPoints(new CoordPolar(distanceFromAlly, angleToAlly),
				new CoordPolar(distanceBetweenAllyAndTarget, angleFromAllyToTarget));
	}
}
