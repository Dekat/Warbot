package edu.warbot.launcher;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import edu.warbot.agents.AliveWarAgent;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.tools.geometry.GeometryTools;
import edu.warbot.tools.geometry.WarStar;
import turtlekit.viewer.TKDefaultViewer;
import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarProjectile;
import edu.warbot.agents.actions.MovableActions;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.gui.MapExplorationListener;
import edu.warbot.gui.WarViewerEvolvedLauncher;
import edu.warbot.gui.debug.DebugModeToolBar;
import edu.warbot.gui.toolbar.WarToolBar;
import edu.warbot.launcher.WarMain.Shared;
import edu.warbot.tools.geometry.CoordCartesian;

@SuppressWarnings("serial")
public class WarViewer extends TKDefaultViewer {

    // Définit le niveau de zoom initial
    public static final int CELL_SIZE = 1;

    private static final int _healthBarDefaultSize = 10;
    private static final int _spaceBetweenAgentAndHealthBar = 2;

    private WarToolBar wtb;
    private DebugModeToolBar _autorModeToolBar;

    private MouseListener _mapExploremMouseListener;

    //private Graphics _mapDisplay;
    private ArrayList<Shape> _explosions;

    private ArrayList<Integer> _agentsIDsSeenBySelectedAgent;

    private JTabbedPane tabs;

    private SwingView swingView;
    private JScrollPane scrollPane;

    private JPanel gdxContainer;

    private int width, height;

    private boolean loadGdx;

    private WarGame game;

    public WarViewer() {
        super();
        this.game = Shared.getGame();
        wtb = new WarToolBar(this);
        _autorModeToolBar = new DebugModeToolBar(this);
        _explosions = new ArrayList<>();
        _agentsIDsSeenBySelectedAgent = new ArrayList<>();

        loadGdx = game.getSettings().isEnabledEnhancedGraphism();
    }

    @Override
    public void setupFrame(final JFrame frame) {
        super.setupFrame(frame);
        width = getWidth();
        height = getHeight();
        wtb.init(frame);
        _autorModeToolBar.init(frame);
        setCellSize(CELL_SIZE);
        ((JScrollPane) getDisplayPane()).getHorizontalScrollBar().setUnitIncrement(10);
        ((JScrollPane) getDisplayPane()).getVerticalScrollBar().setUnitIncrement(10);
        swingView = new SwingView(game);
        swingView.setSize(new Dimension(width, height));

        gdxContainer = new JPanel();

        frame.remove(getDisplayPane());

        scrollPane = new JScrollPane(swingView);
        scrollPane.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                updateSize(e.getPoint(),e.getWheelRotation());
            }
        });

        tabs = new JTabbedPane();
        tabs.addTab("Vue standard", scrollPane);
        tabs.addTab("2D Isométrique", gdxContainer);
        gdxContainer.setSize(1024, 768);

        frame.add(tabs, BorderLayout.CENTER);

        _mapExploremMouseListener = new MapExplorationListener(this);
        setMapExplorationEventsEnabled(true);

        frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        if (loadGdx) {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new WarViewerEvolvedLauncher(gdxContainer, gdxContainer.getSize().width, gdxContainer.getSize().height, game);
                }
            });
        } else {
            showNoGdxLoaded();
        }
    }

    private void showNoGdxLoaded() {
        JLabel text = new JLabel("Vue isométrique non chargée");
        gdxContainer.add(text);
    }

    private void updateSize(Point point, int wheelRotation) {
        final int i = point.x / cellSize;
        int offX = (int) i * wheelRotation;
        int offY = (int) (point.y / cellSize) * wheelRotation;
        cellSize -= wheelRotation;
        if (cellSize < 1)
            cellSize = 1;
        if (cellSize > 1) {
            swingView.setLocation(swingView.getLocation().x + offX,
                    scrollPane.getLocation().y + offY);
        }
        else{
            swingView.setLocation(0, 0);
        }
        swingView.setPreferredSize(new Dimension(getWidth() * cellSize, getHeight() * cellSize));
        swingView.getParent().doLayout();
    }

    @Override
    protected void observe() {
        if (isSynchronousPainting()) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        swingView.repaint();
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            swingView.repaint();
        }
    }

    @Override
    protected void activate() {
        super.activate();
        setSynchronousPainting(false);
    }

    @Override
    protected void render(Graphics g) {
        // Avec observe redefini, render n est plus appelee. L'affichage est
        // effectue dans SwingView.paintComponent
    }

    private void paintTeam(Graphics g, Team team) {
        Graphics2D g2d = (Graphics2D) g;

        Color backgroundColor = team.getColor();
        Color borderColor = backgroundColor.darker();
        Color perceptsColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(),
                backgroundColor.getBlue(), 100);
        boolean isCurrentAgentTheSelectedOne = false;
        boolean haveOneColorChanged = false;

        for(WarAgent agent : team.getAllAgents()) {

            // Si les couleurs ont été modifiées, on restaure les couleurs
            if (haveOneColorChanged) {
                backgroundColor = team.getColor();
                borderColor = backgroundColor.darker();
                isCurrentAgentTheSelectedOne = false;
                haveOneColorChanged = false;
            }

            if (_autorModeToolBar.isVisible()) {
                if (_autorModeToolBar.getSelectedAgent() != null) {
                    if (agent.getID() == _autorModeToolBar.getSelectedAgent().getID()) {
                        borderColor = Color.GRAY;
                        backgroundColor = Color.WHITE;
                        isCurrentAgentTheSelectedOne = true;
                        haveOneColorChanged = true;
                    }
                }
            }

            // Si l'agent courant est vu par l'agent sélectionné
            if (_agentsIDsSeenBySelectedAgent.contains(agent.getID())) {
                borderColor = Color.YELLOW;
                haveOneColorChanged = true;
            }

            if (agent instanceof AliveWarAgent) {
                if (wtb.isShowHealthBars())
                    paintHealthBar(g2d, (AliveWarAgent) agent);
                if (wtb.isShowInfos())
                    paintInfos(g2d, (AliveWarAgent) agent, backgroundColor);
            }

            if (agent instanceof ControllableWarAgent) {
                if(wtb.isShowPercepts())
                    paintPerceptionArea(g2d, (ControllableWarAgent) agent, perceptsColor);
                if (wtb.isShowDebugMessages() && !isCurrentAgentTheSelectedOne)
                    paintDebugMessage(g2d, (ControllableWarAgent) agent);
            }

            Shape agentShape = GeometryTools.resize(agent.getActualForm(), cellSize);
            g2d.setColor(backgroundColor);
            g2d.fill(agentShape);
            g2d.setColor(borderColor);
            g2d.draw(agentShape);

            if (agent instanceof MovableActions) {
                paintHeading(g2d, agent, borderColor);
            }
        }

        if (_autorModeToolBar.getSelectedAgent() != null) {
            if (_autorModeToolBar.getSelectedAgent() instanceof ControllableWarAgent)
                paintDebugMessage(g2d, (ControllableWarAgent) _autorModeToolBar.getSelectedAgent());
        }

        // Affichage des agents mourants
        for (WarAgent a : team.getDyingAgents()) {
            if (a instanceof WarProjectile)
                _explosions.add(createExplosionShape(a.getPosition(), (int) (((WarProjectile)a).getExplosionRadius() - Team.MAX_DYING_STEP + a.getDyingStep())));
            else
                _explosions.add(createExplosionShape(a.getPosition(), (int) ((a.getDyingStep() + a.getHitboxMinRadius()) * 2)));
        }
    }

    private void paintHeading(Graphics g, WarAgent agent, Color color) {
        g.setColor(color);
        double xPos = agent.getX() * cellSize;
        double yPos = agent.getY() * cellSize;
        double hitboxRadius = agent.getHitboxMinRadius() * cellSize;
        g.drawLine((int) xPos, (int) yPos,
                (int) (xPos + hitboxRadius * Math.cos(Math.toRadians(agent.getHeading()))),
                (int) (yPos + hitboxRadius * Math.sin(Math.toRadians(agent.getHeading()))));
    }

    private void paintHealthBar(Graphics g, AliveWarAgent agent) {
        Color previousColor = g.getColor();
        double xPos = agent.getX() * cellSize;
        double yPos = agent.getY() * cellSize;
        double hitboxRadius = agent.getHitboxMinRadius() * cellSize;
        int healthBarHeight = 3 * cellSize;
        double healthBarWidth = _healthBarDefaultSize * cellSize;
        int healthWidth = (int) (healthBarWidth * (Double.valueOf(agent.getHealth()) / Double.valueOf(agent.getMaxHealth())));
        double xBarPos = xPos - (healthBarWidth / 2);
        double yBarPos = yPos - hitboxRadius - healthBarHeight - (_spaceBetweenAgentAndHealthBar * cellSize);

        if (agent.getHealth() <= (agent.getMaxHealth() * 0.25))
            g.setColor(Color.RED);
        else
            g.setColor(Color.ORANGE);
        g.fillRect((int) xBarPos, (int) yBarPos, (int) healthBarWidth, healthBarHeight);

        g.setColor(Color.GREEN);
        g.fillRect((int) xBarPos, (int) yBarPos, healthWidth, healthBarHeight);

        g.setColor(Color.DARK_GRAY);
        g.drawRect((int) xBarPos, (int) yBarPos, (int) healthBarWidth, healthBarHeight);

        g.setColor(previousColor);
    }

    private void paintDebugMessage(Graphics g, ControllableWarAgent agent) {
        if(agent.getDebugString() != ""){
            String msg = agent.getDebugString();
            Color fontColor = agent.getDebugStringColor();

            int distanceBubbleFromAgent = 20;
            int padding = 2;

            Font font = new Font("Arial", Font.PLAIN, 10);
            FontMetrics metrics = g.getFontMetrics(font);
            Dimension speechBubbleSize = new Dimension(metrics.stringWidth(msg) + (2 * padding), metrics.getHeight() + (2 * padding));

            Color backgroundColor;
            boolean fontIsDark = ((fontColor.getRed() + fontColor.getGreen() + fontColor.getBlue()) / 3) < 127;
            if (fontIsDark)
                backgroundColor = Color.WHITE;
            else
                backgroundColor = Color.BLACK;

            int posX = (int) ((agent.getX()) * cellSize - (5 / cellSize) - speechBubbleSize.width - distanceBubbleFromAgent);
            int posY = (int) ((agent.getY()) * cellSize - (5 / cellSize) - speechBubbleSize.height - distanceBubbleFromAgent);
            g.setColor(Color.BLACK);
            g.drawLine(posX, posY, ((int) agent.getX() * cellSize), ((int) agent.getY() * cellSize));
            g.setColor(backgroundColor);
            g.fillRect(posX, posY, speechBubbleSize.width, speechBubbleSize.height);
            g.setColor(Color.BLACK);
            g.drawRect(posX, posY, speechBubbleSize.width, speechBubbleSize.height);
            g.setColor(fontColor);
            g.setFont(font);
            g.drawString(msg, posX + padding, posY + speechBubbleSize.height - padding);
        }
    }

    public DebugModeToolBar getAutorModeToolBar() {
        return _autorModeToolBar;
    }

    private void paintInfos(Graphics g, AliveWarAgent agent, Color color) {
        g.setColor(color);
        double xPos = agent.getX() * cellSize;
        double yPos = agent.getY() * cellSize;
        g.drawString(agent.getClass().getSimpleName() + " " + agent.getID()
                        + ": " + agent.getTeam().getName() + ", " + agent.getHealth()
                        + " HP, heading: " + (int) agent.getHeading(),
                (int) (xPos + (agent.getHitboxMinRadius() * cellSize)),
                (int) yPos);
    }

    private void paintPerceptionArea(Graphics2D g, ControllableWarAgent agent, Color color) {
        g.setColor(color);
        g.draw(GeometryTools.resize(agent.getPerceptionArea(), cellSize));
    }

    private Shape createExplosionShape(CoordCartesian pos, int radius) {
        int newRadius = radius * cellSize;
        return createStar(10, new CoordCartesian(pos.getX() * cellSize, pos.getY() * cellSize), newRadius, newRadius / 2);
    }

    private void paintExplosionShape(Graphics2D g2d, Shape s) {
        RadialGradientPaint color = new RadialGradientPaint(new CoordCartesian(s.getBounds2D().getCenterX(), s.getBounds2D().getCenterY()),
                (float) s.getBounds2D().getWidth(),
                new float[] {0.0f, 0.5f},
                new Color[] {Color.RED, Color.YELLOW});
        g2d.setPaint(color);
        g2d.fill(s);
    }

    private WarStar createStar(int nbArms, Point2D.Double center, double radiusOuterCircle, double radiusInnerCircle) {
        return new WarStar(nbArms, center, radiusOuterCircle, radiusInnerCircle);
    }

    public void setMapExplorationEventsEnabled(boolean bool) {
        if (bool) {
            Toolkit.getDefaultToolkit().addAWTEventListener((AWTEventListener) _mapExploremMouseListener, AWTEvent.KEY_EVENT_MASK);
            swingView.addMouseListener(_mapExploremMouseListener);
            swingView.addMouseMotionListener((MouseMotionListener) _mapExploremMouseListener);
        } else {
            Toolkit.getDefaultToolkit().removeAWTEventListener((AWTEventListener) _mapExploremMouseListener);
            swingView.removeMouseListener(_mapExploremMouseListener);
            swingView.removeMouseMotionListener((MouseMotionListener) _mapExploremMouseListener);
        }
    }

    public JPanel getSwingView() {
        return swingView;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    class SwingView extends JPanel {

        private WarGame game;

        public SwingView(WarGame game) {
            this.game = game;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            //affichage du nombre de FPS
            g2d.drawString("TPS : " + game.getFPS().toString(), 1, 11);

            if (_autorModeToolBar.getSelectedAgent() != null) {
                // Update de l'affichage des infos sur l'unité sélectionnée
                _autorModeToolBar.getAgentInformationsPanel().update();
                // On récupère la liste des agents vus par l'agent sélectionné
                WarAgent selectedAgent = _autorModeToolBar.getSelectedAgent();
                if (selectedAgent instanceof ControllableWarAgent) {
                    for(WarAgentPercept p : ((ControllableWarAgent) selectedAgent).getPercepts())
                        _agentsIDsSeenBySelectedAgent.add(p.getID());
                }
            }

            // Map forbid area
            g2d.setColor(Color.GRAY);
            g2d.fill(GeometryTools.resize(game.getMap().getMapForbidArea(), cellSize));

            // Affichage de Mère Nature (resources)
            paintTeam(g2d, game.getMotherNatureTeam());

            // Affichage des équipes
            for (Team t : game.getPlayerTeams()) {
                paintTeam(g2d, t);
            }

            // Affichage des explosions
            for (Shape s : _explosions)
                paintExplosionShape(g2d, s);
            _explosions.clear();

            g2d.setColor(Color.RED);
            g2d.drawRect(0, 0, width * cellSize, height * cellSize);

            _agentsIDsSeenBySelectedAgent.clear();
        }

    }

    public WarGame getGame() {
        return game;
    }
}
