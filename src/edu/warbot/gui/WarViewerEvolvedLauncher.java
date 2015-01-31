package edu.warbot.gui;


import javax.swing.JPanel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import edu.warbot.game.WarGame;
import edu.warbot.gui.viewer.WarViewerGdx;


public class WarViewerEvolvedLauncher {
	
	private LwjglCanvas canvas;
	
	public WarViewerEvolvedLauncher(JPanel container, int width, int height, WarGame game) {

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "warbot_viewer_2d";
		cfg.useGL20 = true;
//		cfg.width = WarViewerGdx.WIDTH;
//		cfg.height = WarViewerGdx.HEIGHT;
		
		canvas = new LwjglCanvas(new WarViewerGdx(game), cfg);
		canvas.getCanvas().setSize(width, height);
		container.add(canvas.getCanvas());
			
	}
}
