package edu.warbot.gui.debug.eventlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;
import turtlekit.agr.TKOrganization;
import edu.warbot.agents.WarAgent;
import edu.warbot.game.Game;
import edu.warbot.gui.debug.DebugModeToolBar;

public class DeleteToolMouseListener implements MouseListener {

	private DebugModeToolBar _debugToolBar;
	
	public DeleteToolMouseListener(DebugModeToolBar debugToolBar) {
		_debugToolBar = debugToolBar;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// On sélectionne l'agent sous le clique de souris
		ArrayList<WarAgent> agents = Game.getInstance().getAllAgentsInRadius(
				e.getX() / _debugToolBar.getViewer().getCellSize(),
				e.getY() / _debugToolBar.getViewer().getCellSize(),
				1);
		if (agents.size() > 0) {
			WarAgent agentToDelete = agents.get(0);
			int response = JOptionPane.showConfirmDialog(_debugToolBar,
					"êtes-vous sûr de vouloir supprimer l'agent " + agentToDelete.toString(),
					"Suppression d'un agent",
					JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				agentToDelete.killAgent(agentToDelete);

				// TODO à remplacer par une simple actualisation de l'affichage
				_debugToolBar.getViewer().sendMessage(_debugToolBar.getViewer().getCommunity(), TKOrganization.ENGINE_GROUP,
						TKOrganization.SCHEDULER_ROLE,
						new SchedulingMessage(SchedulingAction.STEP));
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
