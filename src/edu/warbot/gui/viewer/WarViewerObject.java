package edu.warbot.gui.viewer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface WarViewerObject {
	public void render(float delta, OrthographicCamera camera, SpriteBatch batch);
}
