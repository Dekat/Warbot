package edu.warbot.gui.viewer.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.gui.viewer.WarViewerObject;
import edu.warbot.gui.viewer.screens.WarViewerScreen;

public class WarViewerEntityBase extends WarViewerEntityControllable implements WarViewerObject {
	
	private Sprite sprite;

	public WarViewerEntityBase(int id, int patchX, int patchY,
			int teamColor, double heading, int health, int maxHealth) {
		super(id, patchX, patchY, teamColor, heading, health,
				maxHealth);
		
		this.width = 127;
		this.height = 100;

		sprite = new Sprite(WarViewerScreen.GAME_SCREEN.getTexture("base_"
				+ teamsColors[teamColor]));
		
		initInfos();
	}

	@Override
	public void render(float delta, OrthographicCamera camera, SpriteBatch batch) {
		ControllableWarAgent ag = getCurrentWarAgent();
		if (ag != null) {
			update(ag.getX(), ag.getY(), ag.getHeading(), ag.getHealth(), ag.getNbElementsInBag());
		}
		
		batch.begin();
			batch.draw(sprite, x, y, width, height);
		batch.end();
	}

}
