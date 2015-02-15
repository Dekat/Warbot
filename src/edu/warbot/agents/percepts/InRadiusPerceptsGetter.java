package edu.warbot.agents.percepts;

import java.awt.*;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.game.WarGame;
import edu.warbot.tools.geometry.WarCircle;

public class InRadiusPerceptsGetter extends PerceptsGetter {

	public InRadiusPerceptsGetter(ControllableWarAgent agent, WarGame game) {
		super(agent, game);
	}
	
    @Override
    public Shape getPerceptionAreaShape() {
        return new WarCircle(getAgent().getX(), getAgent().getY(), getAgent().getDistanceOfView());
//                Ellipse2D.Double(getAgent().getX() - getAgent().getDistanceOfView(),
//                getAgent().getY() - getAgent().getDistanceOfView(),
//                getAgent().getDistanceOfView() * 2.,
//                getAgent().getDistanceOfView() * 2.);
    }
}
