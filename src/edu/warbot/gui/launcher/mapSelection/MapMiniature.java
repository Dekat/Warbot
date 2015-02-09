package edu.warbot.gui.launcher.mapSelection;

import edu.warbot.maps.AbstractWarMap;
import edu.warbot.tools.GeometryTools;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapMiniature extends JPanel {

    public static final double SIZE_SMALL = 0.5;
    public static final double SIZE_MEDIUM = 1.;
    public static final double SIZE_LARGE = 2.;
    public static final double SIZE_VERY_LARGE = 3.;

    private static final double MAP_MARGIN = 5.;
    private static final double DEFAULT_WIDTH = 200.;
    private static final double DEFAULT_HEIGHT = 120.;

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

        double resizeMultiplier = Math.min((height -(MAP_MARGIN*2.)) / map.getHeight(), (width -(MAP_MARGIN*2.)) / map.getWidth());

        // Map forbid area
        g2d.setColor(Color.GRAY);
        Shape resizedShape = GeometryTools.resize(map.getMapForbidArea(), resizeMultiplier);
        g2d.fill(GeometryTools.translateShape(resizedShape, (width - resizedShape.getBounds2D().getWidth()) / 2., (height - resizedShape.getBounds2D().getHeight()) / 2.));

        // Map borders
        g2d.setColor(Color.RED);
        Shape mapBorders = GeometryTools.resize(new Rectangle2D.Double(0, 0, map.getWidth(), map.getHeight()), resizeMultiplier);
        g2d.draw(GeometryTools.translateShape(mapBorders, (width - mapBorders.getBounds2D().getWidth()) / 2., (height - mapBorders.getBounds2D().getHeight()) / 2.));
    }

    public AbstractWarMap getMap() {
        return map;
    }

    public void setMap(AbstractWarMap map) {
        this.map = map;
        repaint();
    }
}
