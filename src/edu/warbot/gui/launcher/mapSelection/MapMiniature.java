package edu.warbot.gui.launcher.mapSelection;

import edu.warbot.maps.AbstractWarMap;
import edu.warbot.tools.GeometryTools;

import javax.swing.*;
import java.awt.*;

public class MapMiniature extends JPanel {

    public static final double SIZE_SMALL = 1.;
    public static final double SIZE_LARGE = 2.;

    private static final double MAP_MARGIN = 5.;
    private static final double DEFAULT_WIDTH = 100.;
    private static final double DEFAULT_HEIGHT = 60.;

    private AbstractWarMap map;
    private double width;
    private double height;

    public MapMiniature(AbstractWarMap map, double size) {
        super();
        width = DEFAULT_WIDTH * size;
        height = DEFAULT_HEIGHT * size;
        Dimension dimension = new Dimension(((Double) width).intValue(), ((Double) height).intValue());
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        this.map = map;

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        double resizeMultiplier = Math.min((height -(MAP_MARGIN*2.)) / map.getHeight(), (width -(MAP_MARGIN*2.)) / map.getWidth());
        Shape resizedShape = GeometryTools.resize(map.getMapAccessibleArea(), resizeMultiplier);
        g2d.draw(GeometryTools.translateShape(resizedShape, (width - resizedShape.getBounds2D().getWidth()) / 2., (height - resizedShape.getBounds2D().getHeight()) / 2.));
    }

    public AbstractWarMap getMap() {
        return map;
    }

    public void setMap(AbstractWarMap map) {
        this.map = map;
        repaint();
    }
}
