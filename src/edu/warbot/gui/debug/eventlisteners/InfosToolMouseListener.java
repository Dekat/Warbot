package edu.warbot.gui.debug.eventlisteners;

import edu.warbot.agents.WarAgent;
import edu.warbot.gui.debug.DebugModeToolBar;
import edu.warbot.tools.geometry.CoordCartesian;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class InfosToolMouseListener implements MouseListener {

	private DebugModeToolBar _debugToolBar;
	
	public InfosToolMouseListener(DebugModeToolBar debugToolBar) {
		_debugToolBar = debugToolBar;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// On sélectionne l'agent sous le clique de souris
        CoordCartesian mouseClickPosition = _debugToolBar.getViewer().convertClickPositionToMapPosition(e.getX(), e.getY());
		ArrayList<WarAgent> agents = _debugToolBar.getViewer().getGame().getAllAgentsInRadius(mouseClickPosition.getX(), mouseClickPosition.getY(), 3);
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
