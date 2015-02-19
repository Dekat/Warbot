package edu.warbot.gui.toolbar;

import edu.warbot.gui.GuiIconsLoader;
import edu.warbot.gui.debug.SaveSituationButton;
import edu.warbot.launcher.AbstractWarViewer;
import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;
import turtlekit.agr.TKOrganization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Bastien Schummer
 * 
 * Barre d'outil de Warbot
 *
 */
@SuppressWarnings("serial")
public class WarToolBar extends JToolBar implements ActionListener, CollapsiblePanel.CollapsiblePanelListener {

    private CollapsiblePanel mainPanel;
	private JButton _btnEndGame;
	private JToggleButton _btnDisplayInfos;
	private JToggleButton _btnDisplayPercepts;
	private JToggleButton _btnDisplayHealthBars;
	private JToggleButton _btnDisplayDebugMessages;
	private JToggleButton _btnAutorMode;
	private AbstractWarViewer _viewer;

	private TeamsDatasTable _teamsDataTable;

	public WarToolBar(AbstractWarViewer viewer) {
		super();
		_viewer = viewer;
	}

	public void init(JFrame frame) {
        mainPanel = new CollapsiblePanel("Outils");

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.setPreferredSize(new Dimension(400, mainPanel.getPreferredSize().height));
        mainPanel.addCollapsiblePanelListener(this);

        // Logo Warbot
		JLabel lblLogo = new JLabel(GuiIconsLoader.getWarbotLogo());
		lblLogo.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(lblLogo);

		// Affichage des équipes
		_teamsDataTable = new TeamsDatasTable(getViewer().getGame());
		JScrollPane pnlTeams = new JScrollPane(_teamsDataTable);
		pnlTeams.setPreferredSize(new Dimension(pnlTeams.getPreferredSize().width, 20 * 9));
        mainPanel.add(pnlTeams);

		// Panel de jeu
		JPanel pnlGame = new JPanel();
		pnlGame.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Gestion du jeu"));
		_btnEndGame = new JButton("Mettre fin au jeu");
		pnlGame.add(_btnEndGame);
        mainPanel.add(pnlGame);

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
        mainPanel.add(pnlDisplay);

		// Panel d'outils
		JPanel pnlTools = new JPanel();
		pnlTools.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Outils"));
		_btnAutorMode = new JToggleButton("Mode debug");
		pnlTools.add(_btnAutorMode);
		pnlTools.add(new SaveSituationButton(this));
        mainPanel.add(pnlTools);

		loadEvents();

        add(mainPanel);
		frame.add(mainPanel, BorderLayout.EAST);
	}

	private void loadEvents() {
		_btnAutorMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_viewer.getDebugModeToolBar().setVisible(_btnAutorMode.isSelected());
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
				int confirmation = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir arrêter le combat ?", "Demande de confirmation", JOptionPane.YES_NO_OPTION);
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

	public AbstractWarViewer getViewer() {
		return _viewer;
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        getViewer().getFrame().repaint();
    }

    @Override
    public void onCollapse(CollapsiblePanel collapsiblePanel) {
        mainPanel.setPreferredSize(new Dimension(30, mainPanel.getPreferredSize().height));
    }

    @Override
    public void onUnfold(CollapsiblePanel collapsiblePanel) {
        mainPanel.setPreferredSize(new Dimension(400, mainPanel.getPreferredSize().height));
        getViewer().moveMapOffsetTo(getViewer().getMapOffsetX(), getViewer().getMapOffsetY());
    }
}
