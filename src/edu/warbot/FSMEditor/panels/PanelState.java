package edu.warbot.FSMEditor.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import edu.warbot.FSMEditor.models.ModelState;

public class PanelState extends AbstractPanel{

	private static final long serialVersionUID = 1L;

	private ModelState modeleState;
	
	public PanelState(ModelState m) {
		this.modeleState = m;
		position = new Point(new Random().nextInt(600), new Random().nextInt(400));
	}

	public void paint(Graphics g){
		
		g.clearRect(position.x, position.y, size.width, size.height);
			
		if(this.isSelected)
			g.setColor(Color.red);
		else if(isFirstState)
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.black);
		
		g.drawRect(position.x, position.y, size.width, size.height);
		
		
		g.drawString(this.modeleState.getName(), position.x + 1, position.y + 11);
		
		g.drawLine(position.x, position.y + size.height/2
				, position.x + size.width - 1, position.y + size.height/2);
		
		g.drawString(this.modeleState.getPlanName().name(), position.x + 1, position.y + size.height/2 + 11);
		
		
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

	public ModelState getModelState() {
		return this.modeleState;
	}

	public Point position = new Point();

	Dimension size = new Dimension(100, 30);
	
	public boolean isSelected = false;
	public boolean isFirstState = false;

	public void setFirstState(boolean b) {
		isFirstState = b;
	}

}
