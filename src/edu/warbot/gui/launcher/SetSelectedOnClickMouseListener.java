package edu.warbot.gui.launcher;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;

public class SetSelectedOnClickMouseListener implements MouseListener {

	private AbstractButton _btn;
	
	public SetSelectedOnClickMouseListener(AbstractButton btn) {
		_btn = btn;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		_btn.setSelected(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
