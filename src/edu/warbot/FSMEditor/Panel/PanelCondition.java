package edu.warbot.FSMEditor.Panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import edu.warbot.FSMEditor.Modele.ModeleCondition;

public class PanelCondition extends AbstractPanel{

	private ModeleCondition modele;

	private PanelState panelDep;
	private PanelState panelArr;

	public PanelCondition(ModeleCondition m) {
		this.modele = m;
	}

	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		
		if (this.isSelected){
			g.setColor(Color.red);
			g.setStroke(new BasicStroke(3));
		}else{
			g.setColor(Color.black);
			g.setStroke(new BasicStroke(1));
		}
		
		this.positionDep = panelDep.position;
		this.positionArr = panelArr.position;

		g.drawLine(this.positionDep.x, this.positionDep.y, this.positionArr.x,
				this.positionArr.y);

		g.drawString(this.modele.getNom(), (positionArr.x - positionDep.x)/2 + positionDep.x,
				(positionArr.y - positionDep.y)/2 + positionDep.y - 1);

	}

	public Point getPositionDep() {
		return this.positionDep;
	}
	
	public Point getPositionArr() {
		return this.positionArr;
	}

	public void setPanelSourceAndDestination(PanelState s, PanelState d) {
		this.panelDep = s;
		this.panelArr = d;

		this.positionDep = s.position;
		this.positionArr = d.position;
	}
	
	public ModeleCondition getModele(){
		return this.modele;
	}

	Point positionDep = new Point();
	Point positionArr = new Point();

	public boolean isSelected = false;
}
