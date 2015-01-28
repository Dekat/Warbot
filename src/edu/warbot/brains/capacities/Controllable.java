package edu.warbot.brains.capacities;

import java.awt.Color;
import java.util.ArrayList;

import madkit.kernel.AbstractAgent.ReturnCode;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.communications.WarMessage;
import edu.warbot.tools.CoordPolar;

public interface Controllable extends CommonCapacities {

	public ReturnCode sendMessage(int idAgent, String message, String ... content);
	public void broadcastMessageToAll(String message, String ... content);
    public ReturnCode broadcastMessageToAgentType(WarAgentType agentType, String message, String ... content);
    public ReturnCode broadcastMessage(String groupName, String roleName, String message, String ... content);
    public ReturnCode reply(WarMessage warMessage, String message, String ... content);
    public ArrayList<WarMessage> getMessages();
    
	public void setIdNextAgentToGive(int idNextAgentToGive);
	public int getBagSize();
	public int getNbElementsInBag();
	public boolean isBagEmpty();
	public boolean isBagFull();
	
	public void setViewDirection(double viewDirection);
	public double getViewDirection();

	public int getHealth();
	public int getMaxHealth();
	
	public ArrayList<WarPercept> getPerceptsAllies();
	public ArrayList<WarPercept> getPerceptsEnemies();
	public ArrayList<WarPercept> getPerceptsResources();
	public ArrayList<WarPercept> getPerceptsAlliesByType(WarAgentType agentType);
	public ArrayList<WarPercept> getPerceptsEnemiesByType(WarAgentType agentType);
	public ArrayList<WarPercept> getPercepts();
	
	public String getDebugString();
	public void setDebugString(String debugString);
	public Color getDebugStringColor();
	public void setDebugStringColor(Color color);
	
	public CoordPolar getAveragePositionOfUnitInPercept(WarAgentType agentType, boolean ally);
	public CoordPolar getIndirectPositionOfAgentWithMessage(WarMessage message);
	public CoordPolar getTargetedAgentPosition(double angleToAlly, double distanceFromAlly, double angleFromAllyToTarget, double distanceBetweenAllyAndTarget);
}
