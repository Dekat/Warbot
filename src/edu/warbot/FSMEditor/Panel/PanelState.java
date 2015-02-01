package edu.warbot.FSMEditor.Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import edu.warbot.FSMEditor.Modeles.ModeleState;

public class PanelState extends AbstractPanel{

	private static final long serialVersionUID = 1L;

	private ModeleState modeleState;
	
	public PanelState(ModeleState m) {
		this.modeleState = m;
	}

	public void paint(Graphics g){
		
		if(this.isSelected)
			g.setColor(Color.red);
		else
			g.setColor(Color.black);
		
		g.drawRect(position.x, position.y, size.width, size.height);
		
		
		g.drawString(this.modeleState.getName(), position.x + 1, position.y + 11);
		
		g.drawLine(position.x, position.y + size.height/2
				, position.x + size.width, position.y + size.height/2);
		
		g.drawString(this.modeleState.getSimplePlanName(), position.x + 1, position.y + size.height/2 + 11);
		
		
	}
	
	public void setPosition(Point p){
		this.position = p;
	}
	
	public Dimension getSize(){
		return this.size;
	}

	public Point getPosition() {
		return this.position;
	}

	public ModeleState getModele() {
		return this.modeleState;
	}

	public Point position = new Point();

	Dimension size = new Dimension(100, 30);
	
	public boolean isSelected = false;

}
