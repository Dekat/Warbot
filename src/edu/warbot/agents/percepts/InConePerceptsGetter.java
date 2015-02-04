package edu.warbot.agents.percepts;

import java.util.ArrayList;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.game.WarGame;

public class InConePerceptsGetter extends PerceptsGetter {

	public InConePerceptsGetter(ControllableWarAgent agent, WarGame game) {
		super(agent, game);
	}

	@Override
	public ArrayList<WarPercept> getAgentPercepts() {
        ArrayList<WarPercept> percepts = new ArrayList<WarPercept>();

		for (WarAgent agentToTestVisible : getGame().getAllAgentsInRadiusOf(getAgent(), getAgent().getDistanceOfView())) {
			if (agentToTestVisible.getID() != getAgent().getID()) {
				double a, b, c;
				double halfAngleOfView = (getAgent().getAngleOfView() / 2.) + agentToTestVisible.getHitboxRadius();

				// test if angle b is between a and c
				a = getAgent().getHeading() - halfAngleOfView;
				b = getAgent().getPosition().getAngleToPoint(agentToTestVisible.getPosition());
				c = getAgent().getHeading() + halfAngleOfView;
				
				if (b < a) {
					a -= 360;
					c -= 360;
				} else if (b > c) {
					a += 360;
					c += 360;
				}
					
				if (c >= b && b >= a)
                    percepts.add(new WarPercept(getAgent(), agentToTestVisible));
			}
		}
		
		return percepts;
	}
}
