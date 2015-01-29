package edu.warbot.brains;

import java.awt.Color;
import java.util.ArrayList;

import madkit.kernel.AbstractAgent.ReturnCode;
import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.capacities.Controllable;
import edu.warbot.communications.WarMessage;
import edu.warbot.tools.CoordPolar;

public abstract class ControllableWarAgentAdapter extends WarAgentAdapter implements Controllable {
	
	public ControllableWarAgentAdapter(ControllableWarAgent agent) {
		super(agent);
	}
	
	@Override
	protected ControllableWarAgent getAgent() {
		return (ControllableWarAgent) super.getAgent();
	}
	
	@Override
    public ReturnCode sendMessage(int idAgent, String message, String ... content) {
    	return getAgent().sendMessage(idAgent, message, content);
    }
    
	@Override
	public void broadcastMessageToAll(String message, String ... content) {
		getAgent().broadcastMessageToAll(message, content);
	}
	
	@Override
    public ReturnCode broadcastMessageToAgentType(WarAgentType agentType, String message, String ... content) {
    	return getAgent().broadcastMessageToAgentType(agentType, message, content);
    }
    
	@Override
    public ReturnCode broadcastMessage(String groupName, String roleName, String message, String ... content) {
    	return getAgent().broadcastMessage(groupName, roleName, message, content);
    }
    
	@Override
    public ReturnCode reply(WarMessage warMessage, String message, String ... content) {
    	return getAgent().reply(warMessage, message, content);
	}
    
	@Override
    public ArrayList<WarMessage> getMessages() {
    	return getAgent().getMessages();
    }
	
	@Override
	public void setIdNextAgentToGive(int idNextAgentToGive) {
		getAgent().setIdNextAgentToGive(idNextAgentToGive);
	}
	
	@Override
	public int getBagSize() {
		return getAgent().getBagSize();
	}
	
	@Override
	public int getNbElementsInBag() {
		return getAgent().getNbElementsInBag();
	}
	
	@Override
	public boolean isBagEmpty() {
		return getAgent().isBagEmpty();
	}
	
	@Override
	public boolean isBagFull() {
		return getAgent().isBagFull();
	}

	@Override
	public void setViewDirection(double viewDirection) {
		getAgent().setViewDirection(viewDirection);
	}
	
	@Override
	public int getHealth() {
		return getAgent().getHealth();
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsAllies() {
		return getAgent().getPerceptsAllies();
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsEnemies() {
		return getAgent().getPerceptsEnemies();
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsResources() {
		return getAgent().getPerceptsResources();
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsAlliesByType(WarAgentType agentType){
		return getAgent().getPerceptsAlliesByType(agentType);
	}

	@Override
	public ArrayList<WarPercept> getPerceptsEnemiesByType(WarAgentType agentType){
		return getAgent().getPerceptsEnemiesByType(agentType);
	}
	
	@Override
	public ArrayList<WarPercept> getPercepts() {
		return getAgent().getPercepts();
	}
	
	@Override
	public String getDebugString() {
		return getAgent().getDebugString();
	}
	
	@Override
	public void setDebugString(String debugString) {
		getAgent().setDebugString(debugString);
	}
	
	@Override
	public Color getDebugStringColor() {
		return getAgent().getDebugStringColor();
	}

	@Override
	public void setDebugStringColor(Color color) {
		getAgent().setDebugStringColor(color);
	}
	
	@Override
	public CoordPolar getAveragePositionOfUnitInPercept(WarAgentType agentType, boolean ally) {
		return getAgent().getAveragePositionOfUnitInPercept(agentType, ally);
	}
	
	@Override
	public CoordPolar getIndirectPositionOfAgentWithMessage(WarMessage message) {
		return getAgent().getIndirectPositionOfAgentWithMessage(message);
	}
	
	@Override
	public CoordPolar getTargetedAgentPosition(double angleToAlly, double distanceFromAlly, double angleFromAllyToTarget, double distanceBetweenAllyAndTarget) {
		return getAgent().getTargetedAgentPosition(angleToAlly, distanceFromAlly, angleFromAllyToTarget, distanceBetweenAllyAndTarget);
	}

	@Override
	public double getViewDirection() {
		return getAgent().getViewDirection();
	}

	@Override
	public int getMaxHealth() {
		return getAgent().getMaxHealth();
	}
}