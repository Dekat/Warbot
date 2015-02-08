package edu.warbot.agents.percepts;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.MotherNatureTeam;
import edu.warbot.game.WarGame;

public abstract class PerceptsGetter {

	private ControllableWarAgent _agent;
	private WarGame game;

    private boolean perceptsAlreadyGetThisTick;
    private ArrayList<WarPercept> allPercepts;
	private ArrayList<WarPercept> alliesPercepts;
    private ArrayList<WarPercept> enemiesPercepts;
    private ArrayList<WarPercept> resourcesPercepts;

	public PerceptsGetter(ControllableWarAgent agent, WarGame game) {
		_agent = agent;
		this.game = game;
        allPercepts = new ArrayList<WarPercept>();
        alliesPercepts = new ArrayList<WarPercept>();
        enemiesPercepts = new ArrayList<WarPercept>();
        resourcesPercepts = new ArrayList<WarPercept>();
	}

	protected ControllableWarAgent getAgent() {
		return _agent;
	}
	
	protected WarGame getGame() {
		return game;
	}

	public ArrayList<WarPercept> getPercepts() {
        if (! perceptsAlreadyGetThisTick) {
            allPercepts = getAgentPercepts();
            for(WarPercept percept : allPercepts) {
                if(getAgent().isEnemy(percept))
                    enemiesPercepts.add(percept);
                else {
                    if (percept.getTeamName().equals(MotherNatureTeam.NAME))
                        resourcesPercepts.add(percept);
                    else
                        alliesPercepts.add(percept);
                }
            }

            perceptsAlreadyGetThisTick = true;
        }
        Collections.sort(alliesPercepts);
        Collections.sort(enemiesPercepts);
        Collections.sort(resourcesPercepts);
        return allPercepts;
    }

    private ArrayList<WarPercept> getAgentPercepts() {
        ArrayList<WarPercept> percepts = new ArrayList<WarPercept>();

        Area visibleArea = new Area(getPerceptionAreaShape());
        for (WarAgent agentToTestVisible : getGame().getAllAgentsInRadiusOf(getAgent(), getAgent().getDistanceOfView())) {
            if (agentToTestVisible.getID() != getAgent().getID()) {
                Area agentArea = new Area(agentToTestVisible.getActualForm());
                agentArea.intersect(visibleArea);
                if (! agentArea.isEmpty())
                    percepts.add(new WarPercept(getAgent(), agentToTestVisible));
            }
        }

        return percepts;
    }

    public abstract Shape getPerceptionAreaShape();

	public ArrayList<WarPercept> getWarAgentsPercepts(boolean ally) {
		if(! perceptsAlreadyGetThisTick)
			getPercepts();

		if(ally)
			return alliesPercepts;
		else
			return enemiesPercepts;
	}

	public ArrayList<WarPercept> getResourcesPercepts() {
        if(! perceptsAlreadyGetThisTick)
            getPercepts();

		return resourcesPercepts;
	}

	public ArrayList<WarPercept> getPerceptsByType(WarAgentType agentType, boolean ally){
        if(! perceptsAlreadyGetThisTick)
            getPercepts();

		ArrayList<WarPercept> perceptsToReturn = new ArrayList<>();
		ArrayList<WarPercept> perceptsToLoop;

		if(ally)
			perceptsToLoop = alliesPercepts;
		else
			perceptsToLoop = enemiesPercepts;

		for (WarPercept warPercept : perceptsToLoop) {
			if(warPercept.getType().equals(agentType)){
				perceptsToReturn.add(warPercept);
			}
		}

		return perceptsToReturn;
	}

    public void setPerceptsOutdated() {
        perceptsAlreadyGetThisTick = false;
        allPercepts.clear();
        alliesPercepts.clear();
        enemiesPercepts.clear();
        resourcesPercepts.clear();
    }

}