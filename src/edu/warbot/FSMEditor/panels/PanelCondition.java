package edu.warbot.FSMEditor.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import edu.warbot.FSMEditor.models.ModelCondition;

public class PanelCondition extends AbstractPanel{

	private static final long serialVersionUID = 1L;

	private ModelCondition modele;

	private PanelState panelSource;
	private PanelState panelDest;

	public PanelCondition(ModelCondition m) {
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
		
		this.positionDep = panelSource.position;
		this.positionArr = panelDest.position;
		
		int Depx = (int) (positionDep.x + panelSource.size.getWidth()/2);
		int Depy = (int) (positionDep.y + panelSource.size.getHeight()/2);

		int Arrx = (int) (positionArr.x + panelDest.size.getWidth()/2);
		int Arry = (int) (positionArr.y + panelDest.size.getHeight()/2);

		g.drawLine(Depx, Depy, Arrx,
				Arry);

		g.drawString(this.modele.getName(), (positionArr.x - positionDep.x)/2 + positionDep.x,
				(positionArr.y - positionDep.y)/2 + positionDep.y - 1);
		
		
		//Reset les configues d'écriture
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1));
	}

	public Point getPositionDep() {
		return this.positionDep;
	}
	
	public Point getPositionArr() {
		return this.positionArr;
	}

	public void setPanelSourceAndDestination(PanelState s, PanelState d) {
		this.panelSource = s;
		this.panelDest = d;

		this.positionDep = s.position;
		this.positionArr = d.position;
	}
	
	public ModelCondition getModele(){
		return this.modele;
	}

	Point positionDep = new Point();
	Point positionArr = new Point();

	public boolean isSelected = false;
}
