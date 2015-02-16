package edu.warbot.gui.viewer.entities.info;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import edu.warbot.gui.viewer.WarViewerObject;
import edu.warbot.gui.viewer.entities.WarViewerEntityControllable;
import edu.warbot.gui.viewer.screens.WarViewerScreen;

public class WarViewerEntityFoodIndicator implements WarViewerObject {
	private int width;
	private int height;
	private int x, y;
	private Sprite sprite;
	private boolean hasFood;
	private WarViewerEntityControllable entity;
	
	public WarViewerEntityFoodIndicator(WarViewerEntityControllable entity) {
		this.entity = entity;
		
		hasFood = false;
		
		Texture texture = WarViewerScreen.GAME_SCREEN.getTexture("food_indicator");
		width = texture.getWidth();
		height = texture.getHeight();
		sprite = new Sprite(texture);
		
		x = entity.getX() + entity.getWidth() + 2;
		y = entity.getY() + entity.getHeight();
	}

	public void render(float delta, OrthographicCamera camera, SpriteBatch batch) {
		if (hasFood) {
			batch.begin();
				batch.draw(sprite, x, y, width, height);
			batch.end();
		}
	}
	
	public void update(boolean hasFood) {
		x = entity.getX() + entity.getWidth() + 2;
		y = entity.getY() + entity.getHeight();
		this.hasFood = hasFood;
	}
}
