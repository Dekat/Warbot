package edu.warbot.agents.percepts;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.MotherNatureTeam;
import edu.warbot.game.WarGame;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.CoordPolar;
import edu.warbot.tools.GeometryTools;
import edu.warbot.tools.WarMathTools;

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

        Area visibleArea = getPerceptionArea();
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

    public Area getPerceptionArea() {
        return removeWallsHidedAreas(new Area(getPerceptionAreaShape()));
    }

    protected abstract Shape getPerceptionAreaShape();

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

    private Area removeWallsHidedAreas(Area initialPerceptionArea) {
        Shape seenWallsShape = game.getMap().getMapForbidArea();

        Area finalPerceptionArea;

        Area perceptionArea = new Area(initialPerceptionArea);
        perceptionArea.subtract(new Area(seenWallsShape));
        finalPerceptionArea = new Area(perceptionArea);

        double shadowPointsDistance = 60;
        Path2D.Double wallsContoursPath = new Path2D.Double();
        wallsContoursPath.append(seenWallsShape, false);
        PathIterator it = wallsContoursPath.getPathIterator(null);
        double[] coords = new double[6];
        Path2D.Double currentShadow = new Path2D.Double();
        CoordCartesian srcPoint = null;
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            CoordCartesian destPoint = new CoordCartesian(coords[0], coords[1]);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    currentShadow.moveTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    currentShadow.lineTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    currentShadow.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case PathIterator.SEG_QUADTO:
                    currentShadow.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
            }
            if (type == PathIterator.SEG_LINETO || type == PathIterator.SEG_CUBICTO || type == PathIterator.SEG_QUADTO) {
                CoordCartesian srcShadowPoint = WarMathTools.addTwoPoints(srcPoint, new CoordPolar(shadowPointsDistance, getAgent().getPosition().getAngleToPoint(srcPoint)));
                CoordCartesian destShadowPoint = WarMathTools.addTwoPoints(destPoint, new CoordPolar(shadowPointsDistance, getAgent().getPosition().getAngleToPoint(destPoint)));
                currentShadow.lineTo(destShadowPoint.getX(), destShadowPoint.getY());
                currentShadow.lineTo(srcShadowPoint.getX(), srcShadowPoint.getY());
                currentShadow.lineTo(srcPoint.getX(), srcPoint.getY());
                finalPerceptionArea.subtract(new Area(currentShadow));
                currentShadow = new Path2D.Double();
                currentShadow.moveTo(coords[0], coords[1]);
            }
            srcPoint = destPoint;
            it.next();
        }

        if(! finalPerceptionArea.isSingular()) {
            Path2D.Double realPerceptionPath = new Path2D.Double();
            realPerceptionPath.append(finalPerceptionArea, false);
            List<Path2D.Double> singularVisiblePaths = GeometryTools.dividePluralPathIntoSingularPaths(realPerceptionPath);
            for(Path2D.Double path : singularVisiblePaths) {
                Area pathArea = new Area(path);
                pathArea.intersect(new Area(getAgent().getActualForm()));
                if(! pathArea.isEmpty()) {
                    finalPerceptionArea.intersect(new Area(path));
                }
            }
        }

        return finalPerceptionArea;
    }

}