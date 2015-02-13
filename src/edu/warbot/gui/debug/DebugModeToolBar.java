package edu.warbot.gui.debug;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;
import turtlekit.agr.TKOrganization;
import edu.warbot.agents.WarAgent;
import edu.warbot.gui.debug.infos.WarAgentInformationsPnl;
import edu.warbot.launcher.WarViewer;

@SuppressWarnings("serial")
public class DebugModeToolBar extends JToolBar {

	private WarAgent _selectedAgent;

	private WarViewer _viewer;
	private DebugToolsPnl _debugToolsPnl;
	private WarAgentInformationsPnl _agentInfosPnl;

	private MouseListener _currentMouseListener;

	public DebugModeToolBar(WarViewer viewer) {
		super();
		_viewer = viewer;
		_currentMouseListener = null;

		setLayout(new BorderLayout());
		setAlignmentY(CENTER_ALIGNMENT);
		setPreferredSize(new Dimension(300, getPreferredSize().height));

		JLabel title = new JLabel("Mode debug");
		title.setFont(new Font("Arial", Font.BOLD, 28));
		add(title, BorderLayout.NORTH);
		
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BorderLayout());
		_debugToolsPnl = new DebugToolsPnl(this);
		pnlCenter.add(_debugToolsPnl, BorderLayout.NORTH);

		_agentInfosPnl = new WarAgentInformationsPnl(this);
		pnlCenter.add(_agentInfosPnl, BorderLayout.CENTER);
		add(pnlCenter, BorderLayout.CENTER);
	}

	public void init(JFrame frame) {
		frame.add(this, BorderLayout.WEST);
		setVisible(false);
	}

	public DebugToolsPnl getDebugTools() {
		return _debugToolsPnl;
	}

	public WarAgent getSelectedAgent() {
		return _selectedAgent;
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (aFlag) { // Si on affiche le panel
			_agentInfosPnl.update();
			if (_currentMouseListener == null)
				_debugToolsPnl.getInfoToolBtn().setSelected(true);
			else
				_viewer.getSwingView().addMouseListener(_currentMouseListener);
		} else {
			setSelectedAgent(null);;
			if (_currentMouseListener != null) {
				_viewer.getSwingView().removeMouseListener(_currentMouseListener);
			}
		}
	}

	public void setSelectedAgent(WarAgent agent) {
		if (_selectedAgent != agent) {
			_selectedAgent = agent;
			_agentInfosPnl.update();

            getViewer().getFrame().repaint();
		}
	}

	public WarViewer getViewer() {
		return _viewer;
	}

	public WarAgentInformationsPnl getAgentInformationsPanel() {
		return _agentInfosPnl;
	}

	public void setNewMouseListener(MouseListener newMouseListener) {
		if (_currentMouseListener != null)
			_viewer.getSwingView().removeMouseListener(_currentMouseListener);
		_currentMouseListener = newMouseListener;
		setSelectedAgent(null);
		_viewer.getSwingView().addMouseListener(_currentMouseListener);
	}
}
