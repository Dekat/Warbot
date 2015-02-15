package edu.warbot.gui;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;

import edu.warbot.launcher.WarViewer;
import edu.warbot.tools.geometry.CoordCartesian;

public class MapExplorationListener implements MouseMotionListener, MouseListener, AWTEventListener {

	private WarViewer _viewer;

	private ArrayList<Integer> _keyEventsCodes;
	private CoordCartesian _oldMouseDragPosition;

	public MapExplorationListener(WarViewer viewer) {
		_viewer = viewer;

		_keyEventsCodes = new ArrayList<>();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		JScrollPane pnl =  _viewer.getScrollPane();

		CoordCartesian movement = new CoordCartesian(
				_oldMouseDragPosition.getX() - e.getX(),
				_oldMouseDragPosition.getY() - e.getY());
		
		pnl.getHorizontalScrollBar().setValue(pnl.getHorizontalScrollBar().getValue() + (int) movement.getX());
		pnl.getVerticalScrollBar().setValue(pnl.getVerticalScrollBar().getValue() + (int) movement.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (! _keyEventsCodes.contains(e.getKeyCode()))
			_keyEventsCodes.add(e.getKeyCode());

		for (Integer keyCode : _keyEventsCodes) {
			CoordCartesian newViewPos = null;
			JScrollPane pnl =  _viewer.getScrollPane();
			CoordCartesian currentPos = new CoordCartesian(pnl.getHorizontalScrollBar().getValue(),
					pnl.getVerticalScrollBar().getValue());

			if (keyCode == KeyEvent.VK_UP)
				newViewPos = new CoordCartesian(
						currentPos.getX(),
						currentPos.getY() - pnl.getVerticalScrollBar().getUnitIncrement());
			else if (keyCode == KeyEvent.VK_DOWN)
				newViewPos = new CoordCartesian(
						currentPos.getX(),
						currentPos.getY() + pnl.getVerticalScrollBar().getUnitIncrement());
			else if (keyCode == KeyEvent.VK_LEFT)
				newViewPos = new CoordCartesian(
						currentPos.getX() - pnl.getHorizontalScrollBar().getUnitIncrement(),
						currentPos.getY());
			else if (keyCode == KeyEvent.VK_RIGHT)
				newViewPos = new CoordCartesian(
						currentPos.getX() + pnl.getHorizontalScrollBar().getUnitIncrement(),
						currentPos.getY());

			if (newViewPos != null) 
				pnl.getViewport().setViewPosition(newViewPos.toPoint());
		}
	}

	public void keyReleased(KeyEvent e) {
		int index = _keyEventsCodes.indexOf(e.getKeyCode());
		if (_keyEventsCodes.size() > index)
			_keyEventsCodes.remove(index);
	}

	@Override
	public void eventDispatched(AWTEvent e) {
		KeyEvent kEvent = (KeyEvent) e;
		if (kEvent.getID() == KeyEvent.KEY_PRESSED)
			keyPressed(kEvent);
		else if (kEvent.getID() == KeyEvent.KEY_RELEASED)
			keyReleased(kEvent);
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
		_oldMouseDragPosition = new CoordCartesian(e.getX(),e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
