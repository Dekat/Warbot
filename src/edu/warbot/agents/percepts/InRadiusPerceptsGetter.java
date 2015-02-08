package edu.warbot.agents.percepts;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.game.WarGame;

public class InRadiusPerceptsGetter extends PerceptsGetter {

	public InRadiusPerceptsGetter(ControllableWarAgent agent, WarGame game) {
		super(agent, game);
	}
	
//	@Override
//	public ArrayList<WarPercept> getAgentPercepts() {
//        ArrayList<WarPercept> percepts = new ArrayList<WarPercept>();
//		for (WarAgent a : getGame().getAllAgentsInRadiusOf(getAgent(), getAgent().getDistanceOfView())) {
//            percepts.add(new WarPercept(getAgent(), a));
//		}
//		return percepts;
//	}

    @Override
    public Shape getPerceptionAreaShape() {
        return new Ellipse2D.Double(getAgent().getX() - getAgent().getDistanceOfView(),
                getAgent().getY() - getAgent().getDistanceOfView(),
                getAgent().getDistanceOfView() * 2.,
                getAgent().getDistanceOfView() * 2.);
    }
}
