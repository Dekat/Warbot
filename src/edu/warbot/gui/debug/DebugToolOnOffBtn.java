package edu.warbot.gui.debug;

import java.awt.Dimension;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.warbot.gui.GuiIconsLoader;

@SuppressWarnings("serial")
public class DebugToolOnOffBtn extends JToggleButton implements ChangeListener {
	
	private DebugModeToolBar _debugToolBar;
	private MouseListener _toolMouseListener;
	
	public DebugToolOnOffBtn(String iconName, String selectedIconName, String toolTipText, char mnemonic, DebugModeToolBar debugToolBar, MouseListener toolMouseListener) {
		super();
		_debugToolBar = debugToolBar;
		_toolMouseListener = toolMouseListener;
		
		setIcon(GuiIconsLoader.getIcon(iconName));
		setSelectedIcon(GuiIconsLoader.getIcon(selectedIconName));
		setToolTipText(toolTipText);
		setMnemonic(mnemonic);
		setPreferredSize(new Dimension(64, 64));
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
		
		addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (isSelected())
			_debugToolBar.setNewMouseListener(_toolMouseListener);
	}
	
}
