package edu.warbot.gui.toolbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;
import turtlekit.agr.TKOrganization;
import edu.warbot.gui.GuiIconsLoader;
import edu.warbot.gui.debug.SaveSituationButton;
import edu.warbot.launcher.WarViewer;

/**
 * 
 * @author Bastien Schummer
 * 
 * Barre d'outil de Warbot
 *
 */
@SuppressWarnings("serial")
public class WarToolBar extends JToolBar implements ActionListener {

	private JButton _btnEndGame;
	private JToggleButton _btnDisplayInfos;
	private JToggleButton _btnDisplayPercepts;
	private JToggleButton _btnDisplayHealthBars;
	private JToggleButton _btnDisplayDebugMessages;
	private JToggleButton _btnAutorMode;
	private WarViewer _viewer;

	private TeamsDatasTable _teamsDataTable;

	public WarToolBar(WarViewer viewer) {
		super();
		_viewer = viewer;
	}

	public void init(JFrame frame) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(300, getPreferredSize().height));
		setAlignmentX(CENTER_ALIGNMENT);

		// Logo Warbot
		JLabel lblLogo = new JLabel(GuiIconsLoader.getWarbotLogo());
		lblLogo.setAlignmentX(CENTER_ALIGNMENT);
		add(lblLogo);

		// Affichage des équipes
		_teamsDataTable = new TeamsDatasTable(getViewer().getGame());
		JScrollPane pnlTeams = new JScrollPane(_teamsDataTable);
		pnlTeams.setPreferredSize(new Dimension(pnlTeams.getPreferredSize().width, 20 * 9));
		add(pnlTeams);

		// Panel de jeu
		JPanel pnlGame = new JPanel();
		pnlGame.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Gestion du jeu"));
		_btnEndGame = new JButton("Mettre fin au jeu");
		pnlGame.add(_btnEndGame);
		add(pnlGame);

		// Panel d'affichages
		JPanel pnlDisplay = new JPanel();
		_btnDisplayInfos = new JToggleButton("Informations");
        _btnDisplayInfos.addActionListener(this);
		pnlDisplay.add(_btnDisplayInfos);
		_btnDisplayPercepts = new JToggleButton("Percepts");
        _btnDisplayPercepts.addActionListener(this);
		pnlDisplay.add(_btnDisplayPercepts);
		_btnDisplayHealthBars = new JToggleButton("Santé");
        _btnDisplayHealthBars.addActionListener(this);
		pnlDisplay.add(_btnDisplayHealthBars);
		_btnDisplayDebugMessages = new JToggleButton("Messages de debug");
        _btnDisplayDebugMessages.addActionListener(this);
		pnlDisplay.add(_btnDisplayDebugMessages);
		pnlDisplay.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Affichage"));
		add(pnlDisplay);

		// Panel d'outils
		JPanel pnlTools = new JPanel();
		pnlTools.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Outils"));
		_btnAutorMode = new JToggleButton("Mode debug");
		pnlTools.add(_btnAutorMode);
		pnlTools.add(new SaveSituationButton(this));
		add(pnlTools);

		loadEvents();

		frame.add(this, BorderLayout.EAST);
	}

	private void loadEvents() {
		_btnAutorMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_viewer.getAutorModeToolBar().setVisible(_btnAutorMode.isSelected());
				if (_btnAutorMode.isSelected()) {
					_viewer.sendMessage(_viewer.getCommunity(), TKOrganization.ENGINE_GROUP, TKOrganization.SCHEDULER_ROLE,
							new SchedulingMessage(SchedulingAction.PAUSE));
				}
			}
		});
		_btnEndGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_viewer.sendMessage(_viewer.getCommunity(), TKOrganization.ENGINE_GROUP, TKOrganization.SCHEDULER_ROLE,
						new SchedulingMessage(SchedulingAction.PAUSE));
				int confirmation = JOptionPane.showConfirmDialog(_viewer.getDisplayPane(), "Êtes-vous sûr de vouloir arrêter le combat ?", "Demande de confirmation", JOptionPane.YES_NO_OPTION);
				if (confirmation == JOptionPane.YES_OPTION) {
					getViewer().getGame().setGameOver();
				} else {
					_viewer.sendMessage(_viewer.getCommunity(), TKOrganization.ENGINE_GROUP, TKOrganization.SCHEDULER_ROLE,
							new SchedulingMessage(SchedulingAction.RUN));
				}
			}
		});

	}

	public boolean isShowInfos() {
		return _btnDisplayInfos.isSelected();
	}

	public boolean isShowPercepts() {
		return _btnDisplayPercepts.isSelected();
	}

	public boolean isShowHealthBars() {
		return _btnDisplayHealthBars.isSelected();
	}

	public boolean isShowDebugMessages() {
		return _btnDisplayDebugMessages.isSelected();
	}

	public TeamsDatasTable getTeamsDatasTable() {
		return _teamsDataTable;
	}

	public WarViewer getViewer() {
		return _viewer;
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        getViewer().getFrame().repaint();
    }
}
