package edu.warbot.gui.debug.eventlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import edu.warbot.tools.geometry.CoordCartesian;
import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;
import turtlekit.agr.TKOrganization;
import edu.warbot.agents.WarAgent;
import edu.warbot.gui.debug.DebugModeToolBar;

public class DeleteToolMouseListener implements MouseListener {

	private DebugModeToolBar _debugToolBar;
	
	public DeleteToolMouseListener(DebugModeToolBar debugToolBar) {
		_debugToolBar = debugToolBar;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// On sélectionne l'agent sous le clique de souris
        CoordCartesian mouseClickPosition = _debugToolBar.getViewer().convertClickPositionToMapPosition(e.getX(), e.getY());
		ArrayList<WarAgent> agents = _debugToolBar.getViewer().getGame().getAllAgentsInRadius(mouseClickPosition.getX(), mouseClickPosition.getY(), 3);
		if (agents.size() > 0) {
			WarAgent agentToDelete = agents.get(0);
			int response = JOptionPane.showConfirmDialog(_debugToolBar,
					"êtes-vous sûr de vouloir supprimer l'agent " + agentToDelete.toString(),
					"Suppression d'un agent",
					JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
                agentToDelete.kill();

                _debugToolBar.getViewer().getFrame().repaint();
			}
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
