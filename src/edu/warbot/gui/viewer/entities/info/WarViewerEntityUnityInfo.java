package edu.warbot.gui.viewer.entities.info;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import edu.warbot.gui.viewer.WarViewerObject;
import edu.warbot.gui.viewer.entities.*;
import edu.warbot.gui.viewer.screens.WarViewerScreen;

public class WarViewerEntityUnityInfo implements WarViewerObject {
	
	private WarViewerEntityControllable entity;
	private TextureRegion texture;
	private int x, y;
	private int width, height;
	
	

	public WarViewerEntityUnityInfo(WarViewerEntityControllable entity) {
		this.entity = entity;
		width = 16;
		height = 16;
		
		initTexture();
		
		initCoordinates();
	}
	
	private void initTexture() {
		Texture t = WarViewerScreen.GAME_SCREEN.getTexture("unities_info");
		TextureRegion tmp[][] = TextureRegion.split(t, 32, 32);
		
		if (entity instanceof WarViewerEntityBase) {
			texture = tmp[0][0];
		} else if (entity instanceof WarViewerEntityExplorer) {
			texture = tmp[0][1];
		} else if (entity instanceof WarViewerEntityEngineer) {
			texture = tmp[0][2];
		} else if (entity instanceof WarViewerEntityRocketLauncher) {
			texture = tmp[0][3];
		} else if (entity instanceof WarViewerEntityKamikaze) {
			texture = tmp[0][4];
		} else if (entity instanceof WarViewerEntityTurret) {
			texture = tmp[0][5];
		}
	}

	private void initCoordinates() {
		x = entity.getX() + entity.getWidth() / 2 - (int) width / 2;
		y = entity.getY() + entity.getHeight() + 20;
	}


	@Override
	public void render(float delta, OrthographicCamera camera, SpriteBatch batch) {
		updatePosition();
		batch.begin();
			batch.draw(texture, x, y, width, height);
		batch.end();
	}

	private void updatePosition() {
		x = entity.getX() + entity.getWidth() / 2 - (int) width / 2;
		y = entity.getY() + entity.getHeight() + 20;
	}
	
}
