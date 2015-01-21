package edu.warbot.agents.percepts;

import java.util.ArrayList;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.game.Game;

public class InRadiusPerceptsGetter extends PerceptsGetter {

	public InRadiusPerceptsGetter(ControllableWarAgent agent) {
		super(agent);
	}
	
	@Override
	public ArrayList<WarPercept> getPercepts() {
		ArrayList<WarPercept> percepts = new ArrayList<>();

		for (WarAgent a : Game.getInstance().getAllAgentsInRadiusOf(getAgent(), getAgent().getDistanceOfView())) {
			percepts.add(new WarPercept(getAgent(), a));
		}

		return percepts;
	}


}
