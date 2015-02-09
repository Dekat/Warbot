package edu.warbot.gui.debug.eventlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;
import turtlekit.agr.TKOrganization;
import edu.warbot.agents.WarAgent;
import edu.warbot.game.WarGame;
import edu.warbot.gui.debug.DebugModeToolBar;
import edu.warbot.tools.CoordCartesian;

public class MoveToolMouseListener implements MouseListener {

	private DebugModeToolBar _debugToolBar;
	private WarAgent _currentSelectedAgent;
	private WarGame game;

	public MoveToolMouseListener(DebugModeToolBar debugToolBar) {
		_debugToolBar = debugToolBar;
		_currentSelectedAgent = null;
		game = _debugToolBar.getViewer().getGame();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		_debugToolBar.getViewer().setMapExplorationEventsEnabled(false);
		
		// On sélectionne l'agent sous le clique de souris
		ArrayList<WarAgent> agents = game.getAllAgentsInRadius(
				e.getX() / _debugToolBar.getViewer().getCellSize(),
				e.getY() / _debugToolBar.getViewer().getCellSize(),
				1);
		if (agents.size() > 0) {
			_currentSelectedAgent = agents.get(0);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (_currentSelectedAgent != null) {
			CoordCartesian newPos = new CoordCartesian(
					e.getX() / _debugToolBar.getViewer().getCellSize(),
					e.getY() / _debugToolBar.getViewer().getCellSize());
			newPos.normalize(0, game.getMap().getWidth() - 1, 0, game.getMap().getHeight() - 1);
			_currentSelectedAgent.setPosition(newPos);
            _currentSelectedAgent.moveOutOfCollision();

            _debugToolBar.getViewer().getFrame().repaint();
		}
		_currentSelectedAgent = null;
		
		_debugToolBar.getViewer().setMapExplorationEventsEnabled(true);
	}

}
