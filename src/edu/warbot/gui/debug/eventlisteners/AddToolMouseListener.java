package edu.warbot.gui.debug.eventlisteners;

import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentCategory;
import edu.warbot.game.WarGame;
import edu.warbot.gui.debug.DebugModeToolBar;
import edu.warbot.gui.debug.DebugToolsPnl;
import edu.warbot.tools.geometry.CoordCartesian;
import edu.warbot.tools.geometry.CoordPolar;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AddToolMouseListener implements MouseListener {

	private DebugModeToolBar _debugToolBar;
	private DebugToolsPnl _toolsPnl;

	private CoordCartesian _clickedPos;
	private String _lastSelectedTeam;
	
	private WarGame game;

	public AddToolMouseListener(DebugModeToolBar debugToolBar, DebugToolsPnl toolsPnl) {
		_debugToolBar = debugToolBar;
		_toolsPnl = toolsPnl;

		_clickedPos = null;
		
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
		if (_toolsPnl.getSelectedWarAgentTypeToCreate() != null) {
			_clickedPos = _debugToolBar.getViewer().convertClickPositionToMapPosition(e.getX(), e.getY());
		} else {
			JOptionPane.showMessageDialog(_debugToolBar, "Veuillez sélectionner un type d'agent.", "Création d'un agent impossible", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			if (_clickedPos != null) {
				if (_toolsPnl.getSelectedWarAgentTypeToCreate().getCategory() == WarAgentCategory.Resource) {
					WarAgent a = game.getMotherNatureTeam().instantiateNewWarResource(_toolsPnl.getSelectedWarAgentTypeToCreate().toString());
					_debugToolBar.getViewer().launchAgent(a);
					a.setPosition(_clickedPos);
                    a.moveOutOfCollision();
				} else {
					CoordCartesian mouseClickPosition = _debugToolBar.getViewer().convertClickPositionToMapPosition(e.getX(), e.getY());
					CoordPolar movement = new CoordCartesian(mouseClickPosition.getX() - _clickedPos.getX(), mouseClickPosition.getY() - _clickedPos.getY()).toPolar();
					String[] choices = game.getPlayerTeamNames();
					if (_lastSelectedTeam == null)
						_lastSelectedTeam = choices[0];
					String teamName = (String) JOptionPane.showInputDialog(_debugToolBar, "A quelle équipe appartient cet agent ?",
							"Choix d'équipe", JOptionPane.QUESTION_MESSAGE, null, choices, _lastSelectedTeam);
					_lastSelectedTeam = teamName;
					WarAgent a = game.getPlayerTeam(teamName).instantiateNewControllableWarAgent(_toolsPnl.getSelectedWarAgentTypeToCreate().toString());
					_debugToolBar.getViewer().launchAgent(a);
					a.setHeading(movement.getAngle());
					a.setPosition(_clickedPos);
                    a.moveOutOfCollision();
				}

                _debugToolBar.getViewer().getFrame().repaint();
			}
		} catch (Exception ex) { // TODO exception la plus précise possible
			System.err.println("Erreur lors de l'instanciation de l'agent " + _toolsPnl.getSelectedWarAgentTypeToCreate().toString());
			ex.printStackTrace();
		}

		_clickedPos = null;
		_debugToolBar.getViewer().setMapExplorationEventsEnabled(true);
	}

}
