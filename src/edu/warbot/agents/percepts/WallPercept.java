package edu.warbot.agents.percepts;

import edu.warbot.agents.ControllableWarAgent;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class WallPercept extends WarPercept {

    private List<Path2D.Double> seenWalls;

    public WallPercept(ControllableWarAgent observer, List<Path2D.Double> seenWalls) {
        super(observer);
        this.seenWalls = seenWalls;
    }

    public List<Path2D.Double> getSeenWalls() {
        return seenWalls;
    }
}
