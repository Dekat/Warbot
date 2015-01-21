package edu.warbot.FSMEditor.Panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import edu.warbot.FSMEditor.Modele.ModeleState;

public class PanelState extends AbstractPanel{

	private ModeleState modele;
	
	public PanelState(ModeleState m) {
		this.modele = m;
	}

	public void paint(Graphics g){
		
		if(this.isSelected)
			g.setColor(Color.red);
		else
			g.setColor(Color.black);
		
		g.drawRect(position.x, position.y, size.width, size.height);
		
		g.drawString(this.modele.getNom(), position.x + 1, position.y + 11);
		
		g.drawLine(position.x, position.y + size.height/2
				, position.x + size.width, position.y + size.height/2);
		
		g.drawString(this.modele.getPlanName(), position.x + 1, position.y + size.height/2 + 11);
		
		
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

	public Point position = new Point();

	Dimension size = new Dimension(100, 30);
	
	public boolean isSelected = false;

	public ModeleState getModele() {
		return this.modele;
	}

}
