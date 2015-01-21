package edu.warbot.gui.debug.eventlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import edu.warbot.agents.WarAgent;
import edu.warbot.game.Game;
import edu.warbot.gui.debug.DebugModeToolBar;

public class InfosToolMouseListener implements MouseListener {

	private DebugModeToolBar _debugToolBar;
	
	public InfosToolMouseListener(DebugModeToolBar debugToolBar) {
		_debugToolBar = debugToolBar;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// On s�lectionne l'agent sous le clique de souris
		ArrayList<WarAgent> agents = Game.getInstance().getAllAgentsInRadius(
				e.getX() / _debugToolBar.getViewer().getCellSize(),
				e.getY() / _debugToolBar.getViewer().getCellSize(),
				1);
		if (agents.size() > 0) {
			_debugToolBar.setSelectedAgent(agents.get(0));
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
