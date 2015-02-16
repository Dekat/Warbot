package edu.warbot.FSMEditor.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.util.Random;

import edu.warbot.FSMEditor.models.ModelCondition;

public class PanelCondition extends AbstractPanel{

	private static final long serialVersionUID = 1L;

	private ModelCondition modele;
	
	PanelState panelSource;
	PanelState panelDest;

	public PanelCondition(ModelCondition m) {
		this.modele = m;
		
		panelSource = modele.getStateSource().getViewState();
		panelDest = modele.getStateDestination().getViewState();
		
		ctrlPoint = new Point(new Random().nextInt(100), new Random().nextInt(75));
		
	}

	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		
		if (this.isSelected){
			g.setColor(Color.red);
			g.setStroke(new BasicStroke(2));
		}else{
			g.setColor(Color.black);
			g.setStroke(new BasicStroke(1));
		}
		
		this.positionDep = panelSource.position;
		this.positionArr = panelDest.position;

		int depx = (int) (positionDep.x + panelSource.size.getWidth()/2);
		int depy = (int) (positionDep.y + panelSource.size.getHeight()/2);

		int arrx = (int) (positionArr.x + panelDest.size.getWidth()/2);
		int arry = (int) (positionArr.y + panelDest.size.getHeight()/2);

		g.drawLine(depx, depy, arrx, arry);	
//		drawLine(g, Depx, Depy, Arrx, Arry);
//		drawArc(g, Depx, Depy, Arrx, Arry);
//		drawBezier(g, Depx, Depy, Arrx, Arry);
		
		g.drawString(this.modele.getName(), (positionArr.x - positionDep.x)/2 + positionDep.x + ctrlPoint.x,
				(positionArr.y - positionDep.y)/2 + positionDep.y - 1 + ctrlPoint.y);
		
		//Reset les configues d'écriture
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1));
	}

	private void drawBezier(Graphics2D g, int x, int y, int arrx, int arry) {
		int c1 = ctrlPoint.x + (arrx - x) + x;
		int c2 = ctrlPoint.y + (arry - y) + y;
				
		Path2D path = new Path2D.Double();
		path.moveTo(x, y);
		path.curveTo(x, y, c1, c2, arrx, arry);
		g.draw(path);
		
	}

	private void drawArc(Graphics2D g, int x1, int y1, int x2, int y2) {
		int x0 = x2 - x1 + x1;
		int y0 = y2 - y1 + y1;
		
		int r = (int)Math.sqrt((x1-x0)*(x1-x0) + (y1-y0)*(y1-y0));
		int x = x0-r;
		int y = y0-r;
		int width = 2*r;
		int height = 2*r;
		int startAngle = (int) (180/Math.PI*Math.atan2(y1-y0, x1-x0));
		int endAngle = (int) (180/Math.PI*Math.atan2(y2-y0, x2-x0));
		g.drawArc(x, y, width, height, startAngle, endAngle);

		
	}

	private void drawLine(Graphics2D g, int depx, int depy, int arrx, int arry) {
		int midX = depx + (arrx - depx)*ctrlPoint.x/100;
		
		g.drawLine(depx, depy, midX, depy);		

		g.drawLine(midX, depy, midX, arry);		
		
		g.drawLine(midX, arry, arrx, arry);		
	}

	public ModelCondition getModele(){
		return this.modele;
	}

	Point positionDep = new Point();
	Point positionArr = new Point();
	
	private Point ctrlPoint = new Point();

	public boolean isSelected = false;
}
