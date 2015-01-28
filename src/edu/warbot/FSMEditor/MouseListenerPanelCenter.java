package edu.warbot.FSMEditor;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import edu.warbot.FSMEditor.Panel.PanelState;

public class MouseListenerPanelCenter implements MouseListener, MouseMotionListener {

	boolean isDragging = false;
	PanelState selectedState;
	
	Point positionClick;
	
	Controleur controleur;
	public MouseListenerPanelCenter(Controleur c) {
		this.controleur = c;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		PanelState selectedState = this.getSelectedItem(e);
		
		if(selectedState == null)
			unselectAllItems();
		else{
			this.selectedState = selectedState;
			this.controleur.view.getPanelCenter().clickOnState(selectedState);
		}
		
		this.controleur.view.getPanelCenter().repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(this.isDragging == false){
			
			
			PanelState dragingState = this.getSelectedItem(e);
			
			if(dragingState != null){
				isDragging = true;
				this.selectedState = dragingState;
			}
			
			this.controleur.view.getPanelCenter().repaint();
		}
	}

	private PanelState getSelectedItem(MouseEvent e) {
		for (PanelState ps : this.controleur.view.getPanelCenter().getPanelState()) {
			
			if(e.getX() > ps.getPosition().x && e.getX() < ps.getPosition().x + ps.getSize().width
					&& e.getY() > ps.getPosition().y && e.getY() < ps.getPosition().y + ps.getSize().height){
				
				this.positionClick = new Point(e.getX() - ps.getPosition().x, e.getY() - ps.getPosition().y);
				
				return ps;
			}
		}
		
		return null;
	}


	private void unselectAllItems() {
		this.controleur.view.getPanelCenter().setNoItemSelected();		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		this.isDragging = false;
			
		//this.putSelectedItem(e);
	
		//this.controleur.view.getPanelCenter().repaint();
	}

	private void putSelectedItem(MouseEvent e) {
		this.selectedState.setPosition(new Point(e.getX() - this.positionClick.x
				,e.getY() - this.positionClick.y));
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		if(this.isDragging){
			this.dragSlectedItem(e);
			this.controleur.view.getPanelCenter().repaint();
		}
	}

	private void dragSlectedItem(MouseEvent e) {
		this.selectedState.setPosition(new Point(e.getX() - this.positionClick.x
				,e.getY() - this.positionClick.y));
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
