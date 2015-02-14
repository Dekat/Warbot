package edu.warbot.agents.percepts;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private ArrayList<WarAgentPercept> allPercepts;
	private ArrayList<WarAgentPercept> alliesPercepts;
    private ArrayList<WarAgentPercept> enemiesPercepts;
    private ArrayList<WarAgentPercept> resourcesPercepts;
    private ArrayList<WallPercept> wallsPercepts;

	public PerceptsGetter(ControllableWarAgent agent, WarGame game) {
		_agent = agent;
		this.game = game;
        allPercepts = new ArrayList<>();
        alliesPercepts = new ArrayList<>();
        enemiesPercepts = new ArrayList<>();
        resourcesPercepts = new ArrayList<>();
        wallsPercepts = new ArrayList<>();
	}

	protected ControllableWarAgent getAgent() {
		return _agent;
	}
	
	protected WarGame getGame() {
		return game;
	}

	public ArrayList<WarAgentPercept> getPercepts() {
        if (! perceptsAlreadyGetThisTick) {
            allPercepts = getAgentPercepts();
            for(WarAgentPercept percept : allPercepts) {
                if(getAgent().isEnemy(percept))
                    enemiesPercepts.add(percept);
                else {
                    if (percept.getType().equals(WarAgentType.WarFood))
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

    private ArrayList<WarAgentPercept> getAgentPercepts() {
        ArrayList<WarAgentPercept> percepts = new ArrayList<WarAgentPercept>();

        Area visibleArea = getPerceptionArea();
        for (WarAgent agentToTestVisible : getGame().getAllAgentsInRadiusOf(getAgent(), getAgent().getDistanceOfView())) {
            if (agentToTestVisible.getID() != getAgent().getID()) {
                Area agentArea = new Area(agentToTestVisible.getActualForm());
                agentArea.intersect(visibleArea);
                if (! agentArea.isEmpty())
                    percepts.add(new WarAgentPercept(getAgent(), agentToTestVisible));
            }
        }

        return percepts;
    }

    public Area getPerceptionArea() {
        if(thisTickPerceptionArea == null)
            thisTickPerceptionArea = removeWallsHidedAreasAndGetWallPercepts(new Area(getPerceptionAreaShape()));
        return thisTickPerceptionArea;
    }

    protected abstract Shape getPerceptionAreaShape();

	public ArrayList<WarAgentPercept> getWarAgentsPercepts(boolean ally) {
		if(! perceptsAlreadyGetThisTick)
			getPercepts();

		if(ally)
			return alliesPercepts;
		else
			return enemiesPercepts;
	}

	public ArrayList<WarAgentPercept> getResourcesPercepts() {
        if(! perceptsAlreadyGetThisTick)
            getPercepts();

		return resourcesPercepts;
	}

	public ArrayList<WarAgentPercept> getPerceptsByType(WarAgentType agentType, boolean ally){
        if(! perceptsAlreadyGetThisTick)
            getPercepts();

		ArrayList<WarAgentPercept> perceptsToReturn = new ArrayList<>();
		ArrayList<WarAgentPercept> perceptsToLoop;

		if(ally)
			perceptsToLoop = alliesPercepts;
		else
			perceptsToLoop = enemiesPercepts;

		for (WarAgentPercept warPercept : perceptsToLoop) {
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
        wallsPercepts.clear();
    }

    public ArrayList<WallPercept> getWallsPercepts() {
        return wallsPercepts;
    }

    private Area removeWallsHidedAreasAndGetWallPercepts(Area initialPerceptionArea) {
        Area finalPerceptionArea;
        ArrayList<Line2D.Double> seenWalls = new ArrayList<>();

        Area seenWallsArea = game.getMap().getMapForbidArea();
        seenWallsArea.intersect(initialPerceptionArea);

        finalPerceptionArea = new Area(initialPerceptionArea);

        ArrayList<Area> shadowsAreas = new ArrayList<>();
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
                Area currentShadowArea = new Area(currentShadow);
                finalPerceptionArea.subtract(currentShadowArea);
                shadowsAreas.add(currentShadowArea);
                currentShadow = new Path2D.Double();
                currentShadow.moveTo(coords[0], coords[1]);
                seenWalls.add(new Line2D.Double(srcPoint, destPoint));
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

        // Get WallsPercepts
        HashMap<Point2D.Double, Boolean> wallPointsSeenByAgent = new HashMap<>();
        for(Point2D.Double wallPoint : GeometryTools.getPointsFromPath(wallsContoursPath)) {
            Line2D.Double lineFromPointToAgent = new Line2D.Double(wallPoint, getAgent().getPosition());
            boolean pointSeenByAgent = true;
            for(Line2D.Double comparedWall : seenWalls) {
                if(! (comparedWall.getP1().equals(wallPoint) || comparedWall.getP2().equals(wallPoint))) { // Si le point n'appartient pas au segment
                    if(lineFromPointToAgent.intersectsLine(comparedWall)) {
                        pointSeenByAgent = false;
                        break;
                    }
                }
            }
            wallPointsSeenByAgent.put(wallPoint, pointSeenByAgent);
        }
        for(Line2D.Double wall : seenWalls) {
            if(wallPointsSeenByAgent.get(wall.getP1()) && wallPointsSeenByAgent.get(wall.getP2())) {
                wallsPercepts.add(new WallPercept(getAgent(), wall));
            }
        }

        return finalPerceptionArea;
    }

    private ArrayList<Path2D.Double> dividePluralPathIntoSingularPathsLined(Path2D.Double path) {
        ArrayList<Path2D.Double> singularPaths = new ArrayList<Path2D.Double>();

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