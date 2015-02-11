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

    private Area thisTickPerceptionArea;
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
        if(thisTickPerceptionArea == null)
            thisTickPerceptionArea = removeWallsHidedAreas(new Area(getPerceptionAreaShape()));
        return thisTickPerceptionArea;
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
        thisTickPerceptionArea = null;
        perceptsAlreadyGetThisTick = false;
        allPercepts.clear();
        alliesPercepts.clear();
        enemiesPercepts.clear();
        resourcesPercepts.clear();
    }

    private Area removeWallsHidedAreas(Area initialPerceptionArea) {
        Area finalPerceptionArea;

        Area seenWallsArea = game.getMap().getMapForbidArea();
        seenWallsArea.intersect(initialPerceptionArea);

        finalPerceptionArea = new Area(initialPerceptionArea);

        double shadowPointsDistance = 100;
        Path2D.Double wallsContoursPath = new Path2D.Double();
        wallsContoursPath.append(seenWallsArea, false);
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
                case PathIterator.SEG_CUBICTO:
                case PathIterator.SEG_QUADTO:
                    currentShadow.lineTo(coords[0], coords[1]);
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
            List<Path2D.Double> singularVisiblePaths = dividePluralPathIntoSingularPathsLined(realPerceptionPath);
            for(Path2D.Double path : singularVisiblePaths) {
                Area pathArea = new Area(path);
                pathArea.intersect(new Area(getAgent().getActualForm()));
                if(! pathArea.isEmpty()) {
                    finalPerceptionArea.intersect(new Area(path));
                }
            }
        }

        finalPerceptionArea.subtract(seenWallsArea);
        return finalPerceptionArea;
    }

    private List<Path2D.Double> dividePluralPathIntoSingularPathsLined(Path2D.Double path) {
        List<Path2D.Double> singularPaths = new ArrayList<Path2D.Double>();

        PathIterator it = path.getPathIterator(null);
        double[] coords = new double[6];
        Path2D.Double currentPath = null;
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    if(currentPath != null)
                        singularPaths.add(new Path2D.Double(currentPath));
                    currentPath = new Path2D.Double();
                    currentPath.moveTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                case PathIterator.SEG_CUBICTO:
                case PathIterator.SEG_QUADTO:
                    currentPath.lineTo(coords[0], coords[1]);
                case PathIterator.SEG_CLOSE:
                    currentPath.closePath();
                    break;
                default:
                    throw new IllegalStateException("unknown PathIterator segment type: " + type);
            }
            it.next();
        }
        if(currentPath != null)
            singularPaths.add(new Path2D.Double(currentPath));

        return singularPaths;
    }


}