package edu.warbot.gui.viewer.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import edu.warbot.gui.viewer.WarViewerObject;
import edu.warbot.gui.viewer.screens.WarViewerScreen;

public class WarViewerEntityFood extends WarViewerEntity implements WarViewerObject {
	
	private Sprite sprite;
	
	public WarViewerEntityFood(int id, int patchX, int patchY, int width,
			int height) {
		this.id = id;
		this.patchX = patchX;
		this.patchY = patchY;
		this.width = width;
		this.height = height;
		
		sprite = new Sprite(WarViewerScreen.GAME_SCREEN.getTexture("food"));
		
		x = getXByPatches();
		y = getYByPatches();
	}

	@Override
	public void render(float delta, OrthographicCamera camera, SpriteBatch batch) {
		batch.begin();
			batch.draw(sprite.getTexture(), getXByPatches(), getYByPatches(), width, height);
		batch.end();
	}

}
