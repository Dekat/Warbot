package edu.warbot.gui.viewer.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import edu.warbot.agents.WarProjectile;
import edu.warbot.game.Team;
import edu.warbot.gui.viewer.WarViewerObject;
import edu.warbot.gui.viewer.animations.WarViewerAnimationExplosion;
import edu.warbot.gui.viewer.screens.WarViewerScreen;

public class WarViewerEntityProjectile extends WarViewerEntity implements WarViewerObject {
	
	private WarProjectile projectile;
	private Team team;
	private Sprite sprite;
	
	public WarViewerEntityProjectile(WarProjectile projectile, int width, int height) {
		this.id = projectile.getID();
		this.patchX = (int) projectile.getX();
		this.patchY = (int) projectile.getY();
		this.width = width;
		this.height = height;
		this.team = projectile.getTeam();
		this.sprite = new Sprite(
				WarViewerScreen.GAME_SCREEN.getTexture("rocket"));
		
		x = getXByPatches();
		y = getYByPatches();
	}

	@Override
	public void render(float delta, OrthographicCamera camera, SpriteBatch batch) {
		updatePosition();
		batch.begin();
			batch.draw(sprite.getTexture(), getXByPatches(), getYByPatches(), width, height);
		batch.end();
	}
	
	private void updatePosition() {
		patchX = (int) projectile.getX();
		patchY = (int) projectile.getY();
	}
	
	public WarViewerAnimationExplosion explode() {
		return new WarViewerAnimationExplosion(getXByPatches(), getYByPatches(), 32, 32, false);
	}

	public Team getTeam() {
		return team;
	}
}
