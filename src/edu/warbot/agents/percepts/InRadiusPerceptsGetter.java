package edu.warbot.agents.percepts;

import java.util.ArrayList;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.game.WarGame;

public class InRadiusPerceptsGetter extends PerceptsGetter {

	public InRadiusPerceptsGetter(ControllableWarAgent agent, WarGame game) {
		super(agent, game);
	}
	
	@Override
	public ArrayList<WarPercept> getAgentPercepts() {
        ArrayList<WarPercept> percepts = new ArrayList<WarPercept>();
		for (WarAgent a : getGame().getAllAgentsInRadiusOf(getAgent(), getAgent().getDistanceOfView())) {
            percepts.add(new WarPercept(getAgent(), a));
		}
		return percepts;
	}


}
