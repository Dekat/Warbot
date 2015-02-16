package edu.warbot.gui;

import edu.warbot.launcher.WarViewer;
import edu.warbot.tools.geometry.CoordCartesian;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MapExplorationListener implements MouseMotionListener, MouseListener, AWTEventListener, MouseWheelListener {

    private static final int MOVE_KEY_INCREMENT = 20;
    private static final int MAX_CELL_SIZE = 10;
    private static final int MIN_CELL_SIZE = 1;

	private WarViewer viewer;

	private ArrayList<Integer> _keyEventsCodes;
    private CoordCartesian oldMouseDragPosition;
    private CoordCartesian oldViewerPosition;

	public MapExplorationListener(WarViewer viewer) {
		this.viewer = viewer;

		_keyEventsCodes = new ArrayList<>();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		CoordCartesian movement = new CoordCartesian(
				e.getX() - oldMouseDragPosition.getX(),
                e.getY() - oldMouseDragPosition.getY());
		viewer.moveMapOffsetTo(movement.getX() + oldViewerPosition.getX(), movement.getY() + oldViewerPosition.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (! _keyEventsCodes.contains(e.getKeyCode()))
			_keyEventsCodes.add(e.getKeyCode());

		for (Integer keyCode : _keyEventsCodes) {
			CoordCartesian newViewPos = null;

			if (keyCode == KeyEvent.VK_UP)
				newViewPos = new CoordCartesian(
                        viewer.getMapOffsetX(),
                        viewer.getMapOffsetY() + MOVE_KEY_INCREMENT);
			else if (keyCode == KeyEvent.VK_DOWN)
				newViewPos = new CoordCartesian(
                        viewer.getMapOffsetX(),
                        viewer.getMapOffsetY() - MOVE_KEY_INCREMENT);
			else if (keyCode == KeyEvent.VK_LEFT)
				newViewPos = new CoordCartesian(
                        viewer.getMapOffsetX() + MOVE_KEY_INCREMENT,
                        viewer.getMapOffsetY());
			else if (keyCode == KeyEvent.VK_RIGHT)
				newViewPos = new CoordCartesian(
                        viewer.getMapOffsetX() - MOVE_KEY_INCREMENT,
                        viewer.getMapOffsetY());

            if (newViewPos != null)
				viewer.moveMapOffsetTo(newViewPos.getX(), newViewPos.getY());
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
		oldMouseDragPosition = new CoordCartesian(e.getX(),e.getY());
        oldViewerPosition = new CoordCartesian(viewer.getMapOffsetX(), viewer.getMapOffsetY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int newCellSize = viewer.getCellSize();
        newCellSize -= e.getWheelRotation();
        newCellSize = Math.max(MIN_CELL_SIZE, newCellSize);
        newCellSize = Math.min(MAX_CELL_SIZE, newCellSize);

        CoordCartesian inMapClickPosition = viewer.convertClickPositionToMapPosition(e.getX(), e.getY());

        viewer.setCellSize(newCellSize);

        CoordCartesian inMapClickNewPosition = viewer.convertClickPositionToMapPosition(e.getX(), e.getY());
        viewer.moveMapOffsetTo(viewer.getMapOffsetX() - ((inMapClickPosition.getX() - inMapClickNewPosition.getX()) * newCellSize),
                viewer.getMapOffsetY() - ((inMapClickPosition.getY() - inMapClickNewPosition.getY()) * newCellSize));
    }
}
