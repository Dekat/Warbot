package edu.warbot.gui.debug;

import edu.warbot.launcher.AbstractWarViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@SuppressWarnings("serial")
public class DebugModeToolBar extends JToolBar {

	private AbstractWarViewer _viewer;
	private DebugToolsPnl _debugToolsPnl;

	private MouseListener _currentViewMouseListener;

	public DebugModeToolBar(AbstractWarViewer viewer) {
		super();
		_viewer = viewer;
		_currentViewMouseListener = null;

		setLayout(new BorderLayout());
		setAlignmentY(CENTER_ALIGNMENT);
		setPreferredSize(new Dimension(300, getPreferredSize().height));

		JLabel title = new JLabel("Mode debug");
		title.setFont(new Font("Arial", Font.BOLD, 28));
		add(title, BorderLayout.NORTH);
		
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BorderLayout());
		_debugToolsPnl = new DebugToolsPnl(this);
		pnlCenter.add(_debugToolsPnl, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);
	}

	public void init(JFrame frame) {
		frame.add(this, BorderLayout.WEST);
		setVisible(false);
	}

	public DebugToolsPnl getDebugTools() {
		return _debugToolsPnl;
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (aFlag) { // Si on affiche le panel
			_debugToolsPnl.getAgentInformationsPanel().update();
			if (_currentViewMouseListener == null)
				_debugToolsPnl.getInfoToolBtn().setSelected(true);
			else {
                addCurrentMouseListener();
            }
		} else {
			_debugToolsPnl.setSelectedAgent(null);
			if (_currentViewMouseListener != null) {
                removeCurrentMouseListener();
			}
		}
	}

	public AbstractWarViewer getViewer() {
		return _viewer;
	}

	public void setNewMouseListener(MouseListener newMouseListener) {
		if (_currentViewMouseListener != null)
			removeCurrentMouseListener();
		_currentViewMouseListener = newMouseListener;
        _debugToolsPnl.setSelectedAgent(null);
        addCurrentMouseListener();
	}

    private void addCurrentMouseListener() {
        _viewer.getDisplayPane().addMouseListener(_currentViewMouseListener);
        if (_currentViewMouseListener instanceof MouseMotionListener)
            _viewer.getDisplayPane().addMouseMotionListener((MouseMotionListener) _currentViewMouseListener);
    }

    private void removeCurrentMouseListener() {
        _viewer.getDisplayPane().removeMouseListener(_currentViewMouseListener);
        if (_currentViewMouseListener instanceof MouseMotionListener)
            _viewer.getDisplayPane().removeMouseMotionListener((MouseMotionListener) _currentViewMouseListener);
    }

}
